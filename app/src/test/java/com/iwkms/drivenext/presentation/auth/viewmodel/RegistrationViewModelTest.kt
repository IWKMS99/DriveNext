package com.iwkms.drivenext.presentation.auth.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.iwkms.drivenext.util.getOrAwaitValue
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class RegistrationViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val viewModel = RegistrationViewModel()

    @Test
    fun `step 1 remains disabled when terms are not accepted`() {
        viewModel.email.value = "demo@drivenext.ru"
        viewModel.password.value = "Demo123"
        viewModel.repeatPassword.value = "Demo123"
        viewModel.termsAccepted.value = false

        val state = viewModel.step1ValidationState.getOrAwaitValue()
        assertFalse(state.isNextButtonEnabled)
    }

    @Test
    fun `build registration summary returns user when all data present`() {
        viewModel.email.value = "demo@drivenext.ru"
        viewModel.password.value = "Demo123"
        viewModel.repeatPassword.value = "Demo123"
        viewModel.termsAccepted.value = true
        viewModel.lastName.value = "Иванов"
        viewModel.firstName.value = "Иван"
        viewModel.birthDate.value = "01/10/1995"
        viewModel.selectedGenderId.value = 1
        viewModel.licenseNumber.value = "1234 567890"
        viewModel.issueDate.value = "10/01/2020"
        viewModel.licensePhotoUri.value = "uri://license"
        viewModel.passportPhotoUri.value = "uri://passport"

        val summary = viewModel.buildRegistrationSummary()
        assertNotNull(summary)
        assertTrue(summary!!.password.isNotEmpty())
        assertTrue(summary.user.name.contains("Иван"))
    }
}
