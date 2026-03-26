package com.dishcovery.app.data.datasource

import com.dishcovery.app.data.datasource.model.RecipeDto

/**
 * Simulates a data source for Recipes
 */
class RecipeProvider {

    companion object {
        private val catProvider = KitchenCategoryProvider

        val recipes = listOf(
            RecipeDto(
                id = 1,
                name = "Pasta Carbonara",
                description = "Clásica pasta italiana con huevo, queso, panceta y pimienta negra.",
                iconUrl = "https://example.com/carbonara.jpg",
                weight = 450,
                categories = listOf(catProvider.categories[0]) // Italiana
            ),
            RecipeDto(
                id = 2,
                name = "Tacos al Pastor",
                description = "Tacos de cerdo marinado con piña, cebolla y cilantro.",
                iconUrl = "https://example.com/tacos.jpg",
                weight = 300,
                categories = listOf(catProvider.categories[1]) // Mexicana
            ),
            RecipeDto(
                id = 3,
                name = "Sushi Roll Salmón",
                description = "Roll de arroz con salmón fresco, aguacate y queso crema.",
                iconUrl = "https://example.com/sushi.jpg",
                weight = 250,
                categories = listOf(catProvider.categories[2]) // Asiática
            ),
            RecipeDto(
                id = 4,
                name = "Ensalada César",
                description = "Lechuga romana, croutons, queso parmesano y aderezo César.",
                iconUrl = "https://example.com/cesar.jpg",
                weight = 350,
                categories = listOf(catProvider.categories[4]) // Saludable
            ),
            RecipeDto(
                id = 5,
                name = "Tiramisú",
                description = "Postre italiano de café con capas de bizcocho y mascarpone.",
                iconUrl = "https://example.com/tiramisu.jpg",
                weight = 200,
                categories = listOf(catProvider.categories[0], catProvider.categories[3]) // Italiana, Postres
            )
        )

        fun getAll(): List<RecipeDto> = recipes

        fun getById(id: Long): RecipeDto? = recipes.find { it.id == id }

        fun getByCategoryId(categoryId: Long): List<RecipeDto> {
            return recipes.filter { recipe -> 
                recipe.categories.any { it.id == categoryId }
            }
        }
    }
}
