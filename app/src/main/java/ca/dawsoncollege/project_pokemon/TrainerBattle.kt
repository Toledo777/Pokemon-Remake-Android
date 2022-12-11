package ca.dawsoncollege.project_pokemon

class TrainerBattle(playerTrainer: PlayerTrainer, val enemyTrainer: EnemyTrainer): Battle(playerTrainer) {

    init{
        enemyTrainer.generateTeam(playerTrainer)
        this.enemyPokemon = enemyTrainer.team[0]
    }

    // check if pokemon fainted, awards xp if so
    // should be called after every move
    override fun checkPokemonFainted(): Boolean {
        if (enemyPokemon.hp == 0) {
            // give player's pokemon exp
            gainExperience()
            // TODO may switch out pokemon automatically here later
            return true
        }
        return false
    }

    override fun gainExperience() {
        val expGained = 2 * (0.3 * this.enemyPokemon.data.baseExperienceReward * this.enemyPokemon.level).toInt()
        // adds exp and levels up if possible
        this.playerPokemon.addExp(expGained)
    }

    // switch out enemy's pokemon when it has reached 0 hp
    fun switchOutEnemyPkm() {
        // remove fainted pokemon from team
        enemyTrainer.team.remove(enemyPokemon)
        // set next pokemon as enemy
        enemyPokemon = enemyTrainer.team[0];
    }

    // switch out player's pokemon when it has reached 0 hp
    // returns true if pokemon > 0 was found, false if not
    fun switchOutPlayerPkm():Boolean {
        // remove fainted pokemon from team
        for (pokemon in playerTrainer.team) {
            if (pokemon.hp > 0) {
                playerPokemon = pokemon
                return true
            }
        }
        // no pokemon was found
        return false
    }
}