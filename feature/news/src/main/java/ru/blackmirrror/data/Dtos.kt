package ru.blackmirrror.data

import ru.blackmirrror.core.api.UserDto

data class PostDto (
    var id            : Long?             = null,
    var latitude      : Double?           = null,
    var longitude     : Double?           = null,
    var title         : String?           = null,
    var description   : String?           = null,
    var imageUrl      : String?           = null,
    var likeCount     : Int?              = null,
    var dateChange    : Int?              = null,
    var dateCreate    : Int?              = null,
    var author        : UserDto?          = null
)

data class PostReviewDto(
    var id         : Long?   = null,
    var comment    : String? = null,
    var dataCreate : String? = null,
    var user       : UserDto? = null,
    var post       : PostDto? = null
)

data class PostWithReviewDto(
    var mark        : PostDto?       = null,
    var wasReviewed : Boolean?       = null,
    var wasLiked    : Boolean?       = null,
    var mrs         : ArrayList<PostReviewDto> = arrayListOf()
)
