package uz.gita.memorygamemn.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.gita.memorygamemn.R
import uz.gita.memorygamemn.data.LevelEnum
import uz.gita.memorygamemn.databinding.ScreenLevelBinding

class LevelScreen : Fragment(R.layout.screen_level) {
    private val binding: ScreenLevelBinding by viewBinding(ScreenLevelBinding::bind)

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.easy.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    v.animate().scaleX(0.90f).scaleY(0.90f).setDuration(50).start()
                } else if (event.action == MotionEvent.ACTION_UP) {
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100)
                        .withEndAction { openGameScreen(LevelEnum.EASY) }
                        .start()
                }
                true
        }
        binding.medium.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                v.animate().scaleX(0.90f).scaleY(0.90f).setDuration(50).start()
            } else if (event.action == MotionEvent.ACTION_UP) {
                v.animate().scaleX(1f).scaleY(1f).setDuration(100)
                    .withEndAction { openGameScreen(LevelEnum.MEDIUM) }
                    .start()
            }
            true
        }
        binding.hard.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                v.animate().scaleX(0.90f).scaleY(0.90f).setDuration(50).start()
            } else if (event.action == MotionEvent.ACTION_UP) {
                v.animate().scaleX(1f).scaleY(1f).setDuration(100)
                    .withEndAction { openGameScreen(LevelEnum.HARD) }
                    .start()
            }
            true
        }
    }

    private fun openGameScreen(level: LevelEnum) {
        findNavController().navigate(LevelScreenDirections.actionLevelScreenToGameScreen(level))
    }
}