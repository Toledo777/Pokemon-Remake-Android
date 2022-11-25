package ca.dawsoncollege.project_pokemon

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApi {

    @GET("/api/v2/pokemon/{name}")
    suspend fun getPokemon(@Path("name") name: String): Response<ApiPokemonData>

    @GET("/api/v2/move/{name}")
    suspend fun getMove(@Path("name") name: String): Response<ApiMoveDetails>
}