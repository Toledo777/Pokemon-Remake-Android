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
    fun switchOutEnemyPkm(): Boolean {
        // remove fainted pokemon from team
        enemyTrainer.team.remove(enemyTrainer.team[0])
        return if (enemyTrainer.team.isNotEmpty()) {
            // set next pokemon as enemy
            enemyPokemon = enemyTrainer.team[0];
            true
        } else
            false
    }

    // switch out player's pokemon when it has reached 0 hp
    fun switchOutPlayerPkm() {
        // remove fainted pokemon from team
        playerTrainer.team.remove(playerPokemon)
        // set next pokemon
        playerPokemon = playerTrainer.team[0];
    }
}