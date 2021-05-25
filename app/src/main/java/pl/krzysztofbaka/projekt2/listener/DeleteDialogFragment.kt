package pl.krzysztofbaka.projekt2.listener

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import pl.krzysztofbaka.projekt2.ImageAdapter
import pl.krzysztofbaka.projekt2.Shared
import kotlin.concurrent.thread

class DeleteDialogFragment(position:Int, adapter : ImageAdapter) : DialogFragment() {
    val toDeletePosition = position
    val imageAdapter = adapter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Czy na pewno chcesz usunąć te zdjęcie ?")
                .setPositiveButton("Usuń",
                    DialogInterface.OnClickListener { dialog, id ->
                        val positionToDel = Shared.list.get(toDeletePosition).id.toString()
                        thread {
                            Shared.db?.image?.delete(positionToDel)
                        }
                        Shared.list.removeAt(toDeletePosition)
                        imageAdapter.images = Shared.list

                    })
                .setNegativeButton("Anuluj",
                    DialogInterface.OnClickListener { dialog, id ->
                    })
            builder.create()
        } ?: throw IllegalStateException("Nie może być null!")
    }
}