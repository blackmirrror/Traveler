package ru.blackmirrror.map.data

import ru.blackmirrror.core.api.UserDto

data class MarkLatLngDto(
    val id: Long,
    val lat: Double,
    val lon: Double
)

data class MarkDto (
    var id            : Long?             = null,
    var latitude      : Double?           = null,
    var longitude     : Double?           = null,
    var title         : String?           = null,
    var description   : String?           = null,
    var imageUrl      : String?           = null,
    var averageRating : Double?           = null,
    var likeCount     : Int?              = null,
    var dateChange    : Int?              = null,
    var dateCreate    : Int?              = null,
    var categories    : Set<MarkCategoryDto> = setOf(),
    var author        : UserDto?          = null
)

data class MarkReviewDto(
    var id         : Long?   = null,
    var rating     : Int?    = null,
    var comment    : String? = null,
    var dataCreate : String? = null,
    var user       : UserDto? = null,
    var mark       : MarkDto? = null
)

data class MarkWithReviewDto(
    var mark        : MarkDto?       = null,
    var wasReviewed : Boolean?       = null,
    var wasLiked    : Boolean?       = null,
    var mrs         : ArrayList<MarkReviewDto> = arrayListOf()
)

enum class MarkCategoryDto {
    CAMPING,
    FISHING,
    HIKE,
    BEACH,
    KAYAK,
    BARBEQUE,
}