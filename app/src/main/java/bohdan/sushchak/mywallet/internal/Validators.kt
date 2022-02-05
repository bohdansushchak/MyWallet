package bohdan.sushchak.mywallet.internal

import java.util.regex.Pattern


enum class ValidBasic {
    valid,
    empty,
    invalid
}

enum class ValidPassword {
    valid,
    empty,
    minLength,
    noNumbers,
    noUpperChar,
    noLowerChar
}

class Validators {

    private val emailPattern = Pattern.compile(
        "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$"
    )

    private val passNumbersPattern = Pattern.compile(".*\\d.*")
    private val passUppercasePattern = Pattern.compile(".*[A-Z].*")
    private val passLowercasePattern = Pattern.compile(".*[a-z].*")

    fun email(text: String): ValidBasic {
        val email = text.trim()

        if (email.isBlank()) {
            return ValidBasic.empty
        }

        if (!emailPattern.matcher(email).matches()) {
            return ValidBasic.invalid
        }

        return ValidBasic.valid
    }

    fun password(text: String): ValidPassword {
        val pass = text.trim()

        if (pass.isBlank()) {
            return ValidPassword.empty
        }

        if (!passNumbersPattern.matcher(pass).matches()) {
            return ValidPassword.noNumbers
        }

        if (!passUppercasePattern.matcher(pass).matches()) {
            return ValidPassword.noUpperChar
        }

        if (!passLowercasePattern.matcher(pass).matches()) {
            return ValidPassword.noLowerChar
        }

        if (pass.length < 6) {
            return ValidPassword.minLength
        }

        return ValidPassword.valid
    }
}