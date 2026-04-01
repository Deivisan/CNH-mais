package com.cnhplus.ui.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.cnhplus.ui.theme.Accent
import com.cnhplus.ui.theme.Primary
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

/**
 * Componente completo de seleção de imagem com câmera OU galeria.
 * Retorna bytes da imagem comprimida (JPEG, max 1MB).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagePickerButton(
    currentImageUrl: String? = null,
    onImageSelected: (ByteArray) -> Unit,
    modifier: Modifier = Modifier,
    size: Int = 120,
    shape: androidx.compose.ui.graphics.Shape = CircleShape
) {
    val context = LocalContext.current
    var showBottomSheet by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && imageUri != null) {
            val bitmap = loadBitmapFromUri(context, imageUri!!)
            bitmap?.let {
                imageBitmap = it
                val bytes = compressBitmap(it, maxSizeKb = 1024)
                onImageSelected(bytes)
            }
        }
    }

    // Gallery launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val bitmap = loadBitmapFromUri(context, it)
            bitmap?.let { bmp ->
                imageBitmap = bmp
                val bytes = compressBitmap(bmp, maxSizeKb = 1024)
                onImageSelected(bytes)
            }
        }
    }

    Box(
        modifier = modifier
            .size(size.dp)
            .clip(shape)
            .background(Color.Gray.copy(alpha = 0.2f))
            .clickable { showBottomSheet = true },
        contentAlignment = Alignment.Center
    ) {
        when {
            imageBitmap != null -> {
                Image(
                    bitmap = imageBitmap!!.asImageBitmap(),
                    contentDescription = "Foto selecionada",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            currentImageUrl != null -> {
                AsyncImage(
                    model = currentImageUrl,
                    contentDescription = "Foto atual",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            else -> {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Adicionar foto",
                    modifier = Modifier.size((size * 0.5).dp),
                    tint = Color.Gray
                )
            }
        }

        // Botão de edição
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(36.dp)
                .clip(CircleShape)
                .background(Primary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = "Editar foto",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    }

    // Bottom sheet de seleção
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = "Escolher Foto",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Opção: Câmera
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            imageUri = createImageUri(context)
                            cameraLauncher.launch(imageUri!!)
                            showBottomSheet = false
                        }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Tirar Foto",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Usar câmera do dispositivo",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Opção: Galeria
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            galleryLauncher.launch("image/*")
                            showBottomSheet = false
                        }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoLibrary,
                        contentDescription = null,
                        tint = Accent,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Escolher da Galeria",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Selecionar foto existente",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

/**
 * Cria URI temporária para captura de câmera via FileProvider.
 */
private fun createImageUri(context: Context): Uri {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_${timeStamp}_"
    val storageDir = File(context.cacheDir, "camera")
    storageDir.mkdirs()
    
    val imageFile = File.createTempFile(imageFileName, ".jpg", storageDir)
    
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        imageFile
    )
}

/**
 * Carrega Bitmap de URI (funciona com FileProvider e galeria).
 */
private fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap? {
    return try {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

/**
 * Comprime bitmap para JPEG com limite de tamanho (KB).
 * Reduz qualidade iterativamente até atingir maxSizeKb.
 */
private fun compressBitmap(bitmap: Bitmap, maxSizeKb: Int = 1024): ByteArray {
    var quality = 90
    var outputBytes: ByteArray
    
    do {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        outputBytes = outputStream.toByteArray()
        quality -= 5
    } while (outputBytes.size / 1024 > maxSizeKb && quality > 10)
    
    return outputBytes
}
