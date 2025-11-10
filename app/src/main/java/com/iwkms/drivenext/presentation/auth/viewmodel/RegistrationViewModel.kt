package com.iwkms.drivenext.presentation.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iwkms.drivenext.domain.model.User
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Locale
import java.util.regex.Pattern

class RegistrationViewModel : ViewModel() {

    val email = MutableLiveData("")
    val password = MutableLiveData("")
    val repeatPassword = MutableLiveData("")
    val termsAccepted = MutableLiveData(false)

    val lastName = MutableLiveData("")
    val firstName = MutableLiveData("")
    val middleName = MutableLiveData("")
    val birthDate = MutableLiveData("")
    val selectedGenderId = MutableLiveData<Int?>()

    val licenseNumber = MutableLiveData("")
    val issueDate = MutableLiveData("")
    val profilePhotoUri = MutableLiveData<String?>(null)
    val licensePhotoUri = MutableLiveData<String?>(null)
    val passportPhotoUri = MutableLiveData<String?>(null)

    private val _step1ValidationState = MediatorLiveData<Step1ValidationState>()
    val step1ValidationState: LiveData<Step1ValidationState> get() = _step1ValidationState

    private val _step2ValidationState = MediatorLiveData<Step2ValidationState>()
    val step2ValidationState: LiveData<Step2ValidationState> get() = _step2ValidationState

    private val _step3ValidationState = MediatorLiveData<Step3ValidationState>()
    val step3ValidationState: LiveData<Step3ValidationState> get() = _step3ValidationState

    init {
        _step1ValidationState.addSource(email) { validateStep1() }
        _step1ValidationState.addSource(password) { validateStep1() }
        _step1ValidationState.addSource(repeatPassword) { validateStep1() }
        _step1ValidationState.addSource(termsAccepted) { validateStep1() }

        _step2ValidationState.addSource(lastName) { validateStep2() }
        _step2ValidationState.addSource(firstName) { validateStep2() }
        _step2ValidationState.addSource(birthDate) { validateStep2() }
        _step2ValidationState.addSource(selectedGenderId) { validateStep2() }

        _step3ValidationState.addSource(licenseNumber) { validateStep3() }
        _step3ValidationState.addSource(issueDate) { validateStep3() }
        _step3ValidationState.addSource(licensePhotoUri) { validateStep3() }
        _step3ValidationState.addSource(passportPhotoUri) { validateStep3() }
    }

    private fun validateStep1() {
        val emailValue = email.value ?: ""
        val passwordValue = password.value ?: ""
        val repeatPasswordValue = repeatPassword.value ?: ""
        val termsAcceptedValue = termsAccepted.value ?: false

        val isEmailValid = isValidEmail(emailValue)
        val isPasswordValid = passwordValue.length >= 6
        val doPasswordsMatch = passwordValue == repeatPasswordValue

        val isNextEnabled =
            isEmailValid && isPasswordValid && doPasswordsMatch && termsAcceptedValue

        _step1ValidationState.value = Step1ValidationState(
            isEmailValid = isEmailValid,
            isPasswordValid = isPasswordValid,
            doPasswordsMatch = doPasswordsMatch,
            isNextButtonEnabled = isNextEnabled
        )
    }

    private fun validateStep2() {
        val isLastNameValid = !lastName.value.isNullOrBlank()
        val isFirstNameValid = !firstName.value.isNullOrBlank()
        val isBirthDateValid = birthDate.value?.let { isValidDate(it, BIRTHDATE_PATTERN) } ?: false
        val isGenderSelected = selectedGenderId.value != null && selectedGenderId.value != -1

        _step2ValidationState.value = Step2ValidationState(
            isNextButtonEnabled = isLastNameValid && isFirstNameValid && isBirthDateValid && isGenderSelected
        )
    }

    private fun validateStep3() {
        val isLicenseNumberValid = !licenseNumber.value.isNullOrBlank()
        val isIssueDateValid = issueDate.value?.let { isValidDate(it, ISSUE_DATE_PATTERN) } ?: false
        val areRequiredPhotosUploaded =
            !licensePhotoUri.value.isNullOrBlank() && !passportPhotoUri.value.isNullOrBlank()

        _step3ValidationState.value = Step3ValidationState(
            isNextButtonEnabled = isLicenseNumberValid && isIssueDateValid && areRequiredPhotosUploaded
        )
    }

    fun buildRegistrationSummary(): RegistrationSummary? {
        val emailValue = email.value?.takeIf { it.isNotBlank() } ?: return null
        val passwordValue = password.value?.takeIf { it.length >= MIN_PASSWORD_LENGTH } ?: return null
        val firstNameValue = firstName.value?.trim().orEmpty()
        val lastNameValue = lastName.value?.trim().orEmpty()
        if (firstNameValue.isEmpty() || lastNameValue.isEmpty()) return null

        val middleNameValue = middleName.value?.takeIf { it.isNotBlank() }
        val displayName = buildString {
            append(firstNameValue)
            middleNameValue?.let {
                append(" ")
                append(it)
            }
            append(" ")
            append(lastNameValue)
        }.trim()

        val joinedDate = LocalDate.now().year.toString()

        val user = User(
            name = displayName,
            email = emailValue,
            avatarUrl = profilePhotoUri.value,
            joinedDate = joinedDate
        )
        return RegistrationSummary(user = user, password = passwordValue)
    }

    private fun isValidDate(value: String, pattern: String): Boolean {
        return runCatching {
            val formatter = SimpleDateFormat(pattern, Locale.getDefault())
            formatter.isLenient = false
            formatter.parse(value)
        }.isSuccess
    }

    private fun isValidEmail(email: String): Boolean {
        return Pattern.matches(
            "[a-zA-Z0-9+._%\\-]{1,256}" +
                    "@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+",
            email
        )
    }

    data class Step1ValidationState(
        val isEmailValid: Boolean = false,
        val isPasswordValid: Boolean = false,
        val doPasswordsMatch: Boolean = false,
        val isNextButtonEnabled: Boolean = false
    )

    data class Step2ValidationState(
        val isNextButtonEnabled: Boolean = false
    )

    data class Step3ValidationState(
        val isNextButtonEnabled: Boolean = false
    )

    data class RegistrationSummary(
        val user: User,
        val password: String
    )

    companion object {
        private const val MIN_PASSWORD_LENGTH = 6
        private const val BIRTHDATE_PATTERN = "MM/dd/yyyy"
        private const val ISSUE_DATE_PATTERN = "dd/MM/yyyy"
    }
}
