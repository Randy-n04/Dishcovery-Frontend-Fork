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
                iconUrl = "https://i.blogs.es/e0be61/salsa_carbonara/1200_900.jpg",
                weight = 450,
                categories = listOf(catProvider.categories[0]) // Italiana
            ),
            RecipeDto(
                id = 2,
                name = "Tacos al Pastor",
                description = "Tacos de cerdo marinado con piña, cebolla y cilantro.",
                iconUrl = "https://lastaquerias.com/wp-content/uploads/2022/11/tacos-pastor-gaacc26fa8_1920.jpg",
                weight = 300,
                categories = listOf(catProvider.categories[1]) // Mexicana
            ),
            RecipeDto(
                id = 3,
                name = "Sushi Roll Salmón",
                description = "Roll de arroz con salmón fresco, aguacate y queso crema.",
                iconUrl = "https://www.oliveradatenea.com/wp-content/uploads/2023/06/Sushi-rolls-de-salmon-y-Olivada-Olivera-dAtenea.jpg",
                weight = 250,
                categories = listOf(catProvider.categories[2]) // Asiática
            ),
            RecipeDto(
                id = 4,
                name = "Ensalada César",
                description = "Lechuga romana, croutons, queso parmesano y aderezo César.",
                iconUrl = "https://recetasdecocina.elmundo.es/wp-content/uploads/2021/07/Ensalada-cesar.jpg",
                weight = 350,
                categories = listOf(catProvider.categories[4]) // Saludable
            ),
            RecipeDto(
                id = 5,
                name = "Tiramisú",
                description = "Postre italiano de café con capas de bizcocho y mascarpone.",
                iconUrl = "https://staticcookist.akamaized.net/wp-content/uploads/sites/22/2024/09/THUMB-VIDEO-2_rev1-56.jpeg",
                weight = 200,
                categories = listOf(catProvider.categories[0], catProvider.categories[3]) // Italiana, Postres
            ),
            RecipeDto(
                id = 6,
                name = "Mondongo",
                description = "Mondongo.",
                iconUrl = "https://preview.redd.it/el-mondongo-vs-skibidi-goku-v0-fszfr9it2ore1.jpg?width=736&format=pjpg&auto=webp&s=8257cad439d905d4e1cf6586164bc96c86810f37",
                weight = 200,
                categories = listOf(catProvider.categories[5]) // Tipica
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
