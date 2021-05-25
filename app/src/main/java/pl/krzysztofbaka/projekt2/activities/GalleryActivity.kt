package pl.krzysztofbaka.projekt2.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import pl.krzysztofbaka.projekt2.ImageAdapter
import pl.krzysztofbaka.projekt2.Shared
import pl.krzysztofbaka.projekt2.databinding.ActivityGalleryBinding
import pl.krzysztofbaka.projekt2.listener.DeleteDialogFragment
import pl.krzysztofbaka.projekt2.listener.RecyclerItemClickListener

class GalleryActivity : AppCompatActivity() {
    val imageAdapter by lazy { ImageAdapter() }
    val binding by lazy { ActivityGalleryBinding.inflate(layoutInflater) }
    lateinit var intentPhotoActivity: Intent


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        imageAdapter.images = Shared.list

        binding.imageList.apply {
            adapter = imageAdapter
        }
        intentPhotoActivity = Intent(this, PhotoActivity::class.java)

        binding.imageList.addOnItemTouchListener(RecyclerItemClickListener(this,binding.imageList,object : RecyclerItemClickListener.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                intentPhotoActivity.putExtra("POSITION",position.toString())
                startActivity(intentPhotoActivity)
            }

            override fun onItemLongClick(view: View?, position: Int) {
                val delete = DeleteDialogFragment(position,imageAdapter)
                val fragment = supportFragmentManager
                delete.show(fragment,"")
            }
        }))
    }

    override fun onResume() {
        super.onResume()
        imageAdapter.images = Shared.list
    }
}