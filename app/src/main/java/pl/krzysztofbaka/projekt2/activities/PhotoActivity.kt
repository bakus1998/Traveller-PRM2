package pl.krzysztofbaka.projekt2.activities

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import pl.krzysztofbaka.projekt2.Shared
import pl.krzysztofbaka.projekt2.databinding.ActivityPhotoBinding

class PhotoActivity : AppCompatActivity() {
    val binding by lazy{ ActivityPhotoBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val position = intent.getStringExtra("POSITION").toString()

        binding.textViewDate.setText(Shared.list.get(position.toInt()).date)
        binding.textViewLoc.setText(Shared.list.get(position.toInt()).loc)
        binding.textViewName.setText(Shared.list.get(position.toInt()).name)
        binding.textViewDescrp.setText(Shared.list.get(position.toInt()).description)

        binding.imageView2.setImageBitmap(
            BitmapFactory.decodeFile(
                Shared.list.get(position.toInt()).path
            )
        )

        binding.buttonBack.setOnClickListener {
            finish()
        }

    }
}