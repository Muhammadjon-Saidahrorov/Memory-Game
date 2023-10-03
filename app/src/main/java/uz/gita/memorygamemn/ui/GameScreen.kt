package uz.gita.memorygamemn.ui

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import at.markushi.ui.CircleButton
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.gita.memorygamemn.R
import uz.gita.memorygamemn.data.CardData
import uz.gita.memorygamemn.data.LevelEnum
import uz.gita.memorygamemn.data.sourse.Shared
import uz.gita.memorygamemn.databinding.ScreenGameBinding
import uz.gita.memorygamemn.domain.AppRepository

class GameScreen : Fragment(R.layout.screen_game) {

    val binding by viewBinding(ScreenGameBinding::bind)
    private val args by navArgs<GameScreenArgs>()
    var level = LevelEnum.EASY
    var _width = 0
    var _height = 0
    val repository = AppRepository()
    var images = arrayListOf<ImageView>()
    var list = arrayListOf<CardData>()
    var countRemovedImages = 0
    var firstImage: ImageView? = null
    var secodImage: ImageView? = null
    lateinit var dialog: Dialog
    var scoreView = 0
    var levelGame = 1
    private var timer: CountDownTimer? = null
    var msFuture: Long = 0
    lateinit var txtScoreInDialog: TextView
    lateinit var txtLevelInDialog: TextView
    lateinit var btnNextInDialog: CircleButton
    private lateinit var  shared : Shared
    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        level = args.level
        dialog = Dialog(requireContext())
        shared = Shared.getInstance(requireContext())
        msFuture = level.time
        binding.space.post {

            _width = binding.container.width / level.horCount
            _height = binding.container.height / level.verCount

            list.addAll(repository.getData(level.horCount * level.verCount))
            describeData(list)
        }



    }

    private fun describeData(list: List<CardData>) {


        binding.progressBar.max = msFuture.toInt()

        binding.textLevel.text = levelGame.toString()


        timer = object : CountDownTimer(msFuture, 1000) {

            override fun onTick(msUntilFinished: Long) {
                binding.textTime.text = (msUntilFinished / 1000).toString()
                binding.progressBar.progress = msUntilFinished.toInt()
            }

            override fun onFinish() {
                binding.textTime.text = ("0")
                showDialogWin("","")

            }
        }.start()


        for (i in 0 until level.horCount) {
            for (j in 0 until level.verCount) {
                var image = ImageView(requireContext())

                binding.container.addView(image)


                var lp = image.layoutParams as ConstraintLayout.LayoutParams

                lp.apply {
                    width = _width
                    height = _height
                }

                image.layoutParams = lp
                image.tag = list[i * level.verCount + j]
                image.setImageResource(R.drawable.p_0)
                //    image.scaleType = ImageView.ScaleType.CENTER_CROP


                image.x = i * _width * 1f
                image.y = j * _height * 1f

                images.add(image)

            }
        }

        addClickListener()

    }

    private fun addClickListener() {
        images.forEach { imageView ->
            imageView.setOnClickListener {
                Log.d("TTT", "click image ${imageView.rotationY}")

                openImage(imageView)

            }

        }

    }


    @SuppressLint("Recycle")
    fun openImage(imageView: ImageView) {
        if (imageView.rotationY == 0f) {
            Log.d("TTT", "openImage ")
            imageView.animate()
                .setDuration(350)
                .rotationY(89f)

                .withEndAction {
                    val data = imageView.tag as CardData
                    imageView.setImageResource(data.imgRes)
                    imageView.rotationY = 271f
                    imageView.animate()
                        .setDuration(350)
                        .rotationY(360f)
                        .start()
                }
                .start()

            if (firstImage == null) {
                firstImage = imageView
            } else {
                secodImage = imageView
            }
        }

        if (firstImage != null && secodImage != null) {
            lifecycleScope.launch {
                for (i in 0 until binding.container.childCount) {
                    val child = binding.container.getChildAt(i)
                    child.isEnabled = false
                }
                delay(1300)
                if ((firstImage!!.tag as CardData).id == (secodImage!!.tag as CardData).id) {
                    countRemovedImages++
                    firstImage!!.visibility = View.GONE
                    secodImage!!.visibility = View.GONE
                    scoreView += 10
                    binding.textScore.text = scoreView.toString()



                    if (countRemovedImages == level.verCount * level.horCount / 2) {
                        val win = MediaPlayer.create(requireContext(),R.raw.uttin)

                        if (shared.getVolume()){

                            win.start()
                        }


                        timer!!.cancel()
                        val downToTimer =
                            ValueAnimator.ofInt(binding.textTime.text.toString().toInt(), 0)
                        downToTimer.addUpdateListener {
                            binding.textTime.text = (it.animatedValue as Int).toString()
                        }
                        downToTimer.interpolator = AccelerateInterpolator()
                        downToTimer.duration = 1000
                        downToTimer.start()

                        val upToScore = ValueAnimator.ofInt(
                            binding.textScore.text.toString().toInt(),
                            binding.textScore.text.toString()
                                .toInt() + binding.textTime.text.toString().toInt()
                        )
                        upToScore.addUpdateListener {
                            binding.textScore.text = (it.animatedValue as Int).toString()
                        }
                        upToScore.interpolator = AccelerateInterpolator()
                        upToScore.duration = 1000
                        upToScore.start()

                        val downToProgressBar =
                            ValueAnimator.ofInt(binding.progressBar.progress, 0)
                        downToProgressBar.addUpdateListener {
                            binding.progressBar.progress = (it.animatedValue as Int)
                        }
                        downToProgressBar.interpolator = AccelerateInterpolator()
                        downToProgressBar.duration = 1000
                        downToProgressBar.start()

                        lifecycleScope.launch {
                            delay(1500)
                            showDialogWin(
                                binding.textScore.text.toString(),
                                (levelGame - 1).toString()
                            )

                        }
                        msFuture -= 10000
                        levelGame++
                        scoreView = binding.textScore.text.toString().toInt()

                    }else {
                        val correct = MediaPlayer.create(requireContext(),R.raw.ashildi)

                        if (shared.getVolume()){

                            correct.start()
                        }
                    }
                } else {
                    val wrong = MediaPlayer.create(requireContext(),R.raw.qate)

                    if (shared.getVolume()){

                        wrong.start()
                    }

                    closeImage(firstImage!!)
                    closeImage(secodImage!!)

                }
                for (i in 0 until binding.container.childCount) {
                    val child = binding.container.getChildAt(i)
                    child.isEnabled = true
                }
                firstImage = null
                secodImage = null
            }


        }

    }

    private fun closeImage(imageView: ImageView) {
        imageView.rotationY = 0f
        imageView.animate()
            .setDuration(350)
            .rotationY(-89f)
            .withEndAction {
                imageView.setImageResource(R.drawable.p_0)
                imageView.rotationY = 91f
                imageView.animate()
                    .setDuration(350)
                    .rotationY(0f)

                    .start()
            }
            .start()
    }


    private fun showDialogWin(score: String, levelGame: String) {


        dialog.setContentView(R.layout.dialog_win)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog.setCancelable(false)
        dialog.show()
        val gameOver = MediaPlayer.create(requireContext(),R.raw.utildin)

        btnNextInDialog = dialog.findViewById(R.id.button_next)
        txtScoreInDialog = dialog.findViewById(R.id.text_score_in_win_dialog)
        txtLevelInDialog = dialog.findViewById(R.id.text_level_in_win_dialog)
        val btnToMenuInDialog = dialog.findViewById<CircleButton>(R.id.button_to_menu)
        val textViewComlated = dialog.findViewById<TextView>(R.id.text_complated)

        txtScoreInDialog.text = binding.textScore.text.toString()
        txtLevelInDialog.text = binding.textLevel.text.toString()

        if (score == "" && levelGame == "") {
            btnNextInDialog.visibility = View.GONE
            btnToMenuInDialog.visibility = View.VISIBLE
            textViewComlated.text = "Game Over"

            if (shared.getVolume()){

                gameOver.start()
            }
        }



        btnNextInDialog.setOnClickListener {
            dialog.dismiss()

            images.clear()
            firstImage = null
            secodImage = null
            countRemovedImages = 0
            _width = binding.container.width / level.horCount
            _height = binding.container.height / level.verCount

            list.clear()
            list.addAll(repository.getData(level.horCount * level.verCount))
            binding.textScore.text = score
            scoreView = score.toInt()
            list.shuffle()
            describeData(list)


        }
        btnToMenuInDialog.setOnClickListener {
            dialog.dismiss()
            gameOver.stop()
            findNavController().popBackStack()

        }

    }

    override fun onPause() {
        super.onPause()
        timer!!.cancel()
        shared.setTime(binding.textTime.text.toString().toLong() * 1000)

    }

    override fun onResume() {
        super.onResume()
        if (timer != null) {
            timer = object : CountDownTimer(shared.getTime(), 1000) {

                override fun onTick(msUntilFinished: Long) {
                    binding.textTime.text = (msUntilFinished / 1000).toString()
                    binding.progressBar.progress = msUntilFinished.toInt()
                }

                override fun onFinish() {
                    binding.textTime.text = ("0")
                    showDialogWin("","")

                }
            }.start()
        }

    }
}