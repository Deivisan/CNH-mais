package com.cnhplus.data.repository

import com.cnhplus.data.BannerDto
import com.cnhplus.network.SupabaseClient

class BannerRepository(private val client: SupabaseClient) {
    
    fun fetchBanners(): Result<List<BannerDto>> {
        return client.get("banners", mapOf("order" to "ordem.asc"))
    }
    
    fun createBanner(banner: BannerDto): Result<BannerDto> {
        return client.insert("banners", banner)
    }
}
