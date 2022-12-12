package ca.dawsoncollege.project_pokemon

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

abstract class Battle(val playerTrainer: PlayerTrainer) {
    // current pokemons in battle
    var playerPokemon = setPlayerPokemon()
    private var playerPokemonIndex = 0;

    lateinit var enemyPokemon: Pokemon

    class SamePokemonException(message: String) : Exception(message)
    class NoPokemonException(message: String) : Exception(message)

    private fun setPlayerPokemon(): Pokemon{
        println("set player pokemon method")
        for (pokemon in playerTrainer.team){
            if (pokemon.hp > 0) {
                this.playerPokemonIndex = this.playerTrainer.team.indexOf(pokemon)
                return pokemon
            }
        }
        throw NoPokemonException("No Pokemon available to fight")
    }

    // switch out players current pokemon
    fun switchSelectPlayerPkm(nextPokemon: Pokemon, i: Int) {
        if (nextPokemon.hp > 0) {
            if (playerPokemonIndex != i){
                playerPokemon = nextPokemon
                playerPokemonIndex = i
            }
            else {
                throw SamePokemonException("${nextPokemon.name} is already in battle!")
            }
        }
        else {
            throw IllegalArgumentException("${nextPokemon.name} is fainted!")
        }
    }

    // switch out player's pokemon when it has reached 0 hp
    // returns true if pokemon > 0 was found, false if not
    fun switchOutPlayerPkm():Boolean {
        // remove fainted pokemon from team
        for (pokemon in playerTrainer.team) {
            if (pokemon.hp > 0 && playerTrainer.team.indexOf(pokemon) != playerPokemonIndex) {
                playerPokemon = pokemon
                playerPokemonIndex = playerTrainer.team.indexOf(pokemon)
                return true
            }
        }
        // no pokemon was found
        return false
    }

    fun updatePlayerPokemon(){
        playerTrainer.team[playerPokemonIndex] = playerPokemon
    }

    // adds 20HP to active pokemon or restores to full health
    fun playerUsePotion() {
        // check to prevent overhealing
        if (playerPokemon.hp + 20 > playerPokemon.battleStat.maxHP) {
            playerPokemon.hp = playerPokemon.battleStat.maxHP
        }
        else {
            playerPokemon.hp += 20;
        }
    }


    suspend fun playerMove(move: Move):Boolean {
        if (move.target == "OPPONENT") {
            // call attackMove and return success status
            return (attackMove(move, this.playerPokemon, this.enemyPokemon))
        }
        else {
            // call friendlyMove and return success status
            return (friendlyMove(move, this.playerPokemon))
        }
    }

    // helper method, takes move, attacker, target as input and attempts attacks
    private suspend fun attackMove(move: Move, attacker: Pokemon, target: Pokemon): Boolean {
        if (moveSuccessCheck(move.accuracy)) {
            val damage = calculateDamage(move, attacker, target)
            // prevent over damage (negative hp)
            if (target.hp  - damage <  0)
                target.hp = 0
            else
                target.hp -= damage
            return true
        }
        // moves missed
        return false
    }

    // helper method, take friendly move and pokemon, use on self
    // returns success status
    private fun friendlyMove(move: Move, pokemon: Pokemon): Boolean {
        if (moveSuccessCheck(move.accuracy)) {
            // check to prevent overheal
            if (pokemon.hp + move.heal > pokemon.battleStat.maxHP)
                pokemon.hp = pokemon.battleStat.maxHP
            else
                pokemon.hp += move.heal
            return true
        }
        return false
    }

    // chose random move to play for enemy, returns success status
    suspend fun playEnemyMove(): String?{
        val moveList = this.enemyPokemon.moveList
        val moveIndex = Random.nextInt(0, 3);

        // hostile move
        if (moveList[moveIndex].target == "OPPONENT") {
            // attempt attack, returns success status
            val success = attackMove(moveList[moveIndex], enemyPokemon, playerPokemon)
            return if (success)
                moveList[moveIndex].name
            else
                null

        }
        // friendly move
        else {
            // enemy pokemon tries heals itself
            val success = friendlyMove(moveList[moveIndex], enemyPokemon)
            return if (success)
                moveList[moveIndex].name
            else
                null
        }
    }

    // leave battle
    fun playerRun(): Battle {
        return this
    }

    // check if move succeeds
    private fun moveSuccessCheck(accuracy: Int): Boolean {
        // 0 accuracy means 100% hit chance in the api
        if (accuracy == 0)
            return true
        val randNum = Random.nextInt(0, 100)
        // check move success using probabilities
        if (accuracy >= randNum) {
            // move succeeds
            return true;
        }
        // move fails
        return false;
    }

    // TODO add type multiplier in milestone 2
    // helper method to calculate physical damage of an attack
    private suspend fun calculateDamage(move: Move, attacker: Pokemon, defender: Pokemon): Int{
        var damage: Double = ((2*attacker.level / 5) + 2) / 50.0
        damage *= move.power

        damage = if (move.damageClass == "PHYSICAL")
            // physical
            damage * (attacker.getBattleStats().attack / defender.battleStat.defense) + 2
        else
            // special
            damage * (attacker.getBattleStats().specialAttack / defender.battleStat.specialAttack) + 2

        // multiply damage with type multiplier
        damage *= getTypeMultiplier(move.type, defender.types[0])

        // if defender has 2nd type, multiply multiplier as well
        if (defender.types.size == 2)
            damage *= getTypeMultiplier(move.type, defender.types[1])
        // return damage as an int
        return damage.toInt()
    }

    // check if enemy has fainted, implementations in derived class
    abstract fun checkPokemonFainted(): Boolean

    // calls addExp with exp gained after defeating enemy pokemon
    abstract fun gainExperience()

    private suspend fun getTypeDamageRelations(type: String): SimplifiedDamageRelations {
        val response = RetrofitInstance.api.getDamageRelations(type)
        if (response.isSuccessful) {
            val ground = response.body()
            val simplifiedRelations = SimplifiedDamageRelations(
                ground!!.name,
                ground.damage_relations.double_damage_to.map {
                    it.name
                },
                ground.damage_relations.half_damage_to.map {
                    it.name
                },
                ground.damage_relations.no_damage_to.map {
                    it.name
                }
            )
            return simplifiedRelations
        } else {
            throw Error("Error! There was a problem.")
        }
    }

    // returns the type multiplier based on move type and target type
    private suspend fun getTypeMultiplier(moveType:String, targetType:String):Double {
        // fetch type relations
        val damageRelations = withContext(Dispatchers.IO) { getTypeDamageRelations(moveType) }

        return if (damageRelations.noEffect.contains(targetType))
            0.0
        else if (damageRelations.notVeryEffective.contains(targetType))
            0.5
        else if (damageRelations.superEffective.contains(targetType))
            2.0
        // return regular damage if type was no present
        else
            1.0
    }
}
