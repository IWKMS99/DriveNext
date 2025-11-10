package com.iwkms.drivenext.presentation.onboarding.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.iwkms.drivenext.R
import com.iwkms.drivenext.data.repository.SessionRepositoryProvider
import com.iwkms.drivenext.databinding.FragmentOnboardingBinding
import com.iwkms.drivenext.presentation.onboarding.adapter.OnboardingAdapter
import kotlinx.coroutines.launch

class OnboardingFragment : Fragment() {

    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!

    private lateinit var onboardingPages: List<Fragment>
    private val sessionRepository by lazy {
        SessionRepositoryProvider.get(requireContext().applicationContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        setupListeners()
        setupIndicators()
        setCurrentIndicator(0)
    }

    private fun setupViewPager() {
        onboardingPages = listOf(
            OnboardingPageFragment.newInstance(
                R.drawable.illustration_onboarding_1,
                getString(R.string.onboarding_1_title),
                getString(R.string.onboarding_1_subtitle)
            ),
            OnboardingPageFragment.newInstance(
                R.drawable.illustration_onboarding_2,
                getString(R.string.onboarding_2_title),
                getString(R.string.onboarding_2_subtitle)
            ),
            OnboardingPageFragment.newInstance(
                R.drawable.illustration_onboarding_3,
                getString(R.string.onboarding_3_title),
                getString(R.string.onboarding_3_subtitle)
            )
        )

        val adapter = OnboardingAdapter(onboardingPages, requireActivity())
        binding.viewPager.adapter = adapter

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)

                if (position == onboardingPages.size - 1) {
                    binding.btnNext.text = getString(R.string.btn_get_started)
                } else {
                    binding.btnNext.text = getString(R.string.btn_next)
                }
            }
        })
    }

    private fun setupIndicators() {
        val indicators = arrayOfNulls<ImageView>(onboardingPages.size)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        layoutParams.setMargins(8, 0, 8, 0)

        for (i in indicators.indices) {
            indicators[i] = ImageView(requireContext())
            indicators[i]?.let {
                it.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.onboarding_indicator_inactive_shape
                    )
                )
                it.layoutParams = layoutParams
                binding.indicatorsContainer.addView(it)
            }
        }
    }

    private fun setCurrentIndicator(position: Int) {
        val childCount = binding.indicatorsContainer.childCount
        for (i in 0 until childCount) {
            val imageView = binding.indicatorsContainer.getChildAt(i) as ImageView
            if (i == position) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.onboarding_indicator_active_shape
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.onboarding_indicator_inactive_shape
                    )
                )
            }
        }
    }


    private fun setupListeners() {
        binding.btnNext.setOnClickListener {
            val currentItem = binding.viewPager.currentItem
            if (currentItem < (binding.viewPager.adapter?.itemCount ?: 0) - 1) {
                binding.viewPager.currentItem = currentItem + 1
            } else {
                navigateToAuth()
            }
        }

        binding.btnSkip.setOnClickListener {
            navigateToAuth()
        }
    }

    private fun navigateToAuth() {
        viewLifecycleOwner.lifecycleScope.launch {
            sessionRepository.setOnboardingCompleted(true)
            findNavController().navigate(R.id.action_onboardingFragment_to_authFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
