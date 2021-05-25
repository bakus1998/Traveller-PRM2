package pl.krzysztofbaka.projekt2

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.krzysztofbaka.projekt2.databinding.ItemPictureBinding
import pl.krzysztofbaka.projekt2.modelDB.Image

class ImageItem(val binding: ItemPictureBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(image: Image){
        binding.apply {
            textviewDate.setText(image.date)
            textViewDescription.setText(image.name)
            picture.setImageBitmap(
                BitmapFactory.decodeFile(
                   image.path
                )
            )
        }
    }
}

class ImageAdapter(): RecyclerView.Adapter<ImageItem>(){
    var images: List<Image> = emptyList()
    set(value){
        field = value
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageItem {
        val binding = ItemPictureBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ImageItem(binding)
    }

    override fun onBindViewHolder(holder: ImageItem, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int =images.size

}