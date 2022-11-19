package ca.dawsoncollege.project_pokemon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ca.dawsoncollege.project_pokemon.databinding.IntroSequenceBinding

class IntroActivity : AppCompatActivity() {
    private lateinit var binding: IntroSequenceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = IntroSequenceBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}