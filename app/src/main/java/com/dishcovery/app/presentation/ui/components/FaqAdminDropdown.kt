package com.dishcovery.app.presentation.ui.components
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dishcovery.app.R
import com.dishcovery.app.domain.model.Faq
import com.dishcovery.app.presentation.ui.theme.Rojo
import com.dishcovery.app.presentation.ui.theme.TextPrimary
import kotlinx.coroutines.delay


@Composable
fun FaqAdminDropdown(
    faq        : Faq?,
    editContent: String,
    isLoading  : Boolean,
    error      : String?,
    saveSuccess: Boolean,
    onContentChange: (String) -> Unit,
    onSave     : () -> Unit,
    onDismissSaveSuccess: () -> Unit,
    modifier   : Modifier = Modifier
) {
    // Toast-like feedback al guardar
    LaunchedEffect(saveSuccess) {
        if (saveSuccess) {
            delay(2000)
            onDismissSaveSuccess()
        }
    }

    AnimatedVisibility(
        visible = true,
        enter   = expandVertically() + fadeIn(),
        exit    = shrinkVertically() + fadeOut(),
        modifier = modifier
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text  = stringResource(R.string.faq_item_admin),
                    style = MaterialTheme.typography.titleSmall,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )

                when {
                    isLoading && faq == null -> {
                        Box(Modifier.fillMaxWidth().height(100.dp), Alignment.Center) {
                            CircularProgressIndicator(color = Rojo, modifier = Modifier.size(24.dp))
                        }
                    }
                    error != null && faq == null -> {
                        Text(error, color = Rojo, fontSize = 13.sp)
                    }
                    else -> {
                        OutlinedTextField(
                            value         = editContent,
                            onValueChange = onContentChange,
                            modifier      = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 120.dp),
                            enabled       = !isLoading,
                            placeholder   = { Text(stringResource(R.string.faq_placeholder), fontSize = 13.sp) },
                            colors        = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor   = Rojo,
                                unfocusedBorderColor = TextPrimary.copy(alpha = 0.3f),
                                cursorColor          = Rojo
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )

                        // Feedback de éxito
                        AnimatedVisibility(visible = saveSuccess) {
                            Text(
                                text  = stringResource(R.string.faq_save_success),
                                color = Color(0xFF4CAF50),
                                fontSize = 13.sp
                            )
                        }

                        // Error al guardar
                        if (error != null) {
                            Text(error, color = Rojo, fontSize = 13.sp)
                        }

                        Button(
                            onClick  = onSave,
                            enabled  = !isLoading,
                            modifier = Modifier.align(Alignment.End),
                            colors   = ButtonDefaults.buttonColors(containerColor = Rojo),
                            shape    = RoundedCornerShape(8.dp)
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    color    = Color.White,
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    stringResource(R.string.faq_save_button),
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}