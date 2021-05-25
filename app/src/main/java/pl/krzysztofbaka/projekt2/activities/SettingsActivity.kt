package pl.krzysztofbaka.projekt2.activities

import android.R
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import pl.krzysztofbaka.projekt2.models.SettingModel
import pl.krzysztofbaka.projekt2.Shared
import pl.krzysztofbaka.projekt2.databinding.ActivitySettingsBinding


class SettingsActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySettingsBinding.inflate(layoutInflater)}
    val sizeOptions = arrayOf("20","30","40","50","60")
    val colorOptions = arrayOf("Czerwony","Biały","Niebieski","Zielony","Żółty")
    lateinit var resultOptionColor: String
    lateinit var resultOptionSize: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.spinnerSize.adapter = ArrayAdapter<String>(this, R.layout.simple_list_item_1, sizeOptions)
        binding.spinnerSize.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
            ) {
                resultOptionSize = sizeOptions.get(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //binding.textViewWybranaOpcja.setText(Shared.transactionList.get(position1).kategoria)
            }
        }


        binding.spinnerColor.adapter = ArrayAdapter<String>(this, R.layout.simple_list_item_1, colorOptions)
        binding.spinnerColor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
            ) {
                resultOptionColor = colorOptions.get(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //binding.textViewWybranaOpcja.setText(Shared.transactionList.get(position1).kategoria)
            }
        }
        setStartSelection()

        binding.seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                binding.textViewRange.setText(binding.seekBar.progress.toString())
            }
        })


        binding.button.setOnClickListener {
            var color = 0;

            if(resultOptionColor.equals("Czerwony")){
                color  = Color.RED
            }else if(resultOptionColor.equals("Biały")){
                color= Color.WHITE
            }else if(resultOptionColor.equals("Niebieski")){
                color= Color.BLUE
            }else if(resultOptionColor.equals("Zielony")){
                color= Color.GREEN
            }else if(resultOptionColor.equals("Żółty")){
                color = Color.YELLOW
            }

            Shared.settings = SettingModel(color,resultOptionSize.toFloat(),binding.textViewRange.text.toString().toInt());
            finish()

        }
    }


    fun setStartSelection(){
        var indexColor =0;
        var indexSize = 0;

        if(Shared.settings?.color ?:Int ==Color.RED){
            indexColor = 0
        }else if(Shared.settings?.color == Color.WHITE){
            indexColor=1
        }else if(Shared.settings?.color == Color.BLUE){
            indexColor=2
        }else if(Shared.settings?.color == Color.GREEN){
            indexColor=3
        }else if(Shared.settings?.color == Color.YELLOW){
            indexColor=4
        }

        for (i in 0..sizeOptions.size - 1) {
            var check = sizeOptions.get(i).toFloat()
            var db = Shared.settings?.size
            if (check == db) {
                indexSize = i
            }
        }
        binding.spinnerColor.setSelection(indexColor)
        binding.spinnerSize.setSelection(indexSize)
    }


    override fun onResume() {
        super.onResume()
    }
}