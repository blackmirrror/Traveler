package ru.blackmirrror.map.domain

import ru.blackmirrror.component.R
import ru.blackmirrror.map.data.MarkCategoryDto

enum class Category(
    val title: String,
    val painterId: Int
) {
    CAMPING("Кэмпинг", R.drawable.ic_camping),
    FISHING("Рыбалка", R.drawable.ic_fishing),
    HIKE("Поход", R.drawable.ic_hike),
    BEACH("Пляж", R.drawable.ic_beach),
    KAYAK("Байдарки", R.drawable.ic_kayak),
    BARBEQUE("Шашлык", R.drawable.ic_barbecue);
}

fun Category.toMarkCategoryDto(): MarkCategoryDto {
    return when (this) {
        Category.CAMPING -> MarkCategoryDto.CAMPING
        Category.FISHING -> MarkCategoryDto.FISHING
        Category.HIKE -> MarkCategoryDto.HIKE
        Category.BEACH -> MarkCategoryDto.BEACH
        Category.KAYAK -> MarkCategoryDto.KAYAK
        Category.BARBEQUE -> MarkCategoryDto.BARBEQUE
    }
}

fun MarkCategoryDto.toCategory(): Category {
    return when (this) {
        MarkCategoryDto.CAMPING -> Category.CAMPING
        MarkCategoryDto.FISHING -> Category.FISHING
        MarkCategoryDto.HIKE -> Category.HIKE
        MarkCategoryDto.BEACH -> Category.BEACH
        MarkCategoryDto.KAYAK -> Category.KAYAK
        MarkCategoryDto.BARBEQUE -> Category.BARBEQUE
    }
}