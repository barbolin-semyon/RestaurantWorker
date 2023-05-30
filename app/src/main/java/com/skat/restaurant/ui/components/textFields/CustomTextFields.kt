package com.skat.restaurant.ui.components.textFields

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.skat.restaurant.ui.theme.Alpha


/**
 * Input text with label and error
 * @param[value] Text value
 * @param[onValueChange] Function to update text
 * @param[visualTransformation] Visual transformation
 * @param[rightIcon] Composable function to display content to the right of the text
 * @param[errorText] Error text
 * @param[isError] Flag to show error text
 * @param[labelText] Label text
 * @param[enabled] Flag to enable the element
 * @param[modifier] Modifier
 * @param[paddingValues] Padding values
 * @param[keyboardOptions] Keyboard options
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TextFieldsWithLabelError(
    value: String,
    onValueChange: (newValue: String) -> Unit = {},
    visualTransformation: VisualTransformation = VisualTransformation.None,
    rightIcon: @Composable () -> Unit = {},
    errorText: String = "",
    textStyle: TextStyle = TextStyle.Default,
    labelText: String = "",
    enabled: Boolean = true,
    isError: Boolean = false,
    modifier: Modifier = Modifier.fillMaxWidth(),
    paddingValues: PaddingValues = PaddingValues(horizontal = 32.dp),
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    keyboardActions: KeyboardActions = KeyboardActions()
) {
    val interactionSource = remember { MutableInteractionSource() }
    val colors = TextFieldDefaults.textFieldColors(
        backgroundColor = Alpha,
        textColor = MaterialTheme.colors.onSurface
    )

    Column {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier
                .padding(paddingValues)
                //.background(colors.backgroundColor(enabled).value, shape)
                .indicatorLine(enabled, false, interactionSource, colors),
            enabled = enabled,
            textStyle = textStyle,
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            decorationBox = @Composable { innerTextField ->
                // places leading icon, text field with label and placeholder, trailing icon
                TextFieldDefaults.TextFieldDecorationBox(
                    value = value,
                    visualTransformation = visualTransformation,
                    innerTextField = innerTextField,
                    placeholder = {  },
                    label = { Text(labelText) },
                    trailingIcon = { rightIcon()  },
                    singleLine = true,
                    enabled = enabled,
                    isError = false,
                    interactionSource = remember { MutableInteractionSource() },
                    contentPadding = PaddingValues(0.dp),
                    colors = colors
                )
            },
        )

        if (isError) {
            Text(
                text = errorText,
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.body2,
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(start = 4.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppTextFields(
    value: String,
    onValueChange: (newValue: String) -> Unit = {},
    visualTransformation: VisualTransformation = VisualTransformation.None,
    rightIcon: @Composable () -> Unit = {},
    labelText: String = "",
    textStyle: TextStyle = TextStyle.Default,
    enabled: Boolean = true,
    modifier: Modifier = Modifier.fillMaxWidth(),
    paddingValues: PaddingValues = PaddingValues(horizontal = 32.dp, vertical = 0.dp),
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    keyboardActions: KeyboardActions = KeyboardActions()
) {
    val interactionSource = remember { MutableInteractionSource() }
    val colors = TextFieldDefaults.textFieldColors(
        backgroundColor = Alpha,
        textColor = MaterialTheme.colors.onSurface
    )

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .padding(paddingValues)
            //.background(colors.backgroundColor(enabled).value, shape)
            .indicatorLine(enabled, false, interactionSource, colors),
        enabled = enabled,
        textStyle = textStyle,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        decorationBox = @Composable { innerTextField ->
            // places leading icon, text field with label and placeholder, trailing icon
            TextFieldDefaults.TextFieldDecorationBox(
                value = value,
                visualTransformation = visualTransformation,
                innerTextField = innerTextField,
                placeholder = {  },
                label = { Text(labelText) },
                trailingIcon = { rightIcon()  },
                singleLine = true,
                enabled = enabled,
                isError = false,
                interactionSource = remember { MutableInteractionSource() },
                contentPadding = PaddingValues(0.dp),
                colors = colors
            )
        },
    )
}

@Composable
fun TextFieldsWithLabelError(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit = {},
    visualTransformation: VisualTransformation = VisualTransformation.None,
    rightIcon: @Composable () -> Unit = {},
    errorText: String = "",
    textStyle: TextStyle = TextStyle.Default,
    labelText: String = "",
    enabled: Boolean = true,
    isError: Boolean = false,
    modifier: Modifier = Modifier.fillMaxWidth(),
    paddingValues: PaddingValues = PaddingValues(horizontal = 32.dp),
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    keyboardActions: KeyboardActions = KeyboardActions()
) {
    Column {
        TextField(
            value = value,
            onValueChange = { text -> onValueChange(text) },
            modifier = modifier.padding(paddingValues),
            enabled = enabled,
            textStyle = textStyle,
            keyboardActions = keyboardActions,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Alpha,
                textColor = MaterialTheme.colors.onSurface
            ),
            trailingIcon = { rightIcon() },
            label = { Text(labelText) },
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            isError = isError
        )

        if (isError) {
            Text(
                text = errorText,
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.body2,
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(start = 4.dp)
            )
        }
    }
}