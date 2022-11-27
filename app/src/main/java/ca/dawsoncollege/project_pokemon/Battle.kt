package ca.dawsoncollege.project_pokemon

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlin.random.Random

abstract class Battle(val playerTrainer: PlayerTrainer, val context: Context) {
    // current pokemons in battle
    var playerPokemon = playerTrainer.team[0]

    lateinit var enemyPokemon: Pokemon


    fun switchOutPlayerPkm(nextPokemon: Pokemon) {
        if (nextPokemon.hp > 0) {
            playerPokemon = nextPokemon
        }
        else {
            throw IllegalArgumentException("Error, Cannot switch to a pokemon with 0 HP")
        }
    }

    fun playerUsePotion() {
        // check to prevent overhealing
        if (playerPokemon.hp + 20 > playerPokemon.battleStat.maxHP) {
            playerPokemon.hp = playerPokemon.battleStat.maxHP
        }
        else {
            playerPokemon.hp += 20;
        }
    }

    // TODO Potentially create method for friendly player moves
    // takes move as input and attempts attacks on enemy
    fun attackMove(move: Move, attacker: Pokemon, target: Pokemon): Boolean {
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

    // take friendly move and pokemon, use on self
    // returns success status
    fun friendlyMove(move: Move, pokemon: Pokemon): Boolean {
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
    fun playEnemyMove(): Boolean{
        val moveList = this.enemyPokemon.moveList
        val moveIndex = Random.nextInt(0, 3);

        // hostile move
        if (moveList[moveIndex].target == "HOSTILE") {
            return attackMove(moveList[moveIndex], enemyPokemon, playerPokemon)
        }
        // friendly move
        else {
            // enemy pokemon tries heals itself
            friendlyMove(moveList[moveIndex], enemyPokemon)
        }
    }

    // leave battle
    fun playerRun() {
        TODO("Not sure if necessary")
    }

    // check if move succeeds
    private fun moveSuccessCheck(accuracy: Int): Boolean {
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
    private fun calculateDamage(move: Move, attacker: Pokemon, defender: Pokemon): Int{
        var damage: Double = ((2*attacker.level / 5) + 2) / 50.0
        damage *= move.power

        damage = if (move.damageClass == "PHYSICAL")
            // physical
            damage * (attacker.getBattleStats().attack / defender.battleStat.defense) + 2
        else
            // special
            damage * (attacker.getBattleStats().specialAttack / defender.battleStat.specialAttack) + 2

        // return damage as an int
        return damage.toInt()
    }

    abstract fun checkPokemonFainted(): Boolean


    fun gainExperience() {
        val expGained = (0.3 * this.enemyPokemon.data.baseExperienceReward * this.enemyPokemon.level).toInt()
        // adds exp and levels up if possible
        this.playerPokemon.addExp(expGained)
    }
}
