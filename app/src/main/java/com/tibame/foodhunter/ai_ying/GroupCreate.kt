@file:OptIn(ExperimentalMaterial3Api::class)

package com.tibame.foodhunter.ai_ying

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tibame.foodhunter.R


@Composable
fun GroupCreate(
    navController: NavHostController,
    groupViewModel: GroupViewModel = viewModel()
) {
    Text(text = stringResource(R.string.str_create_group))
}

@Preview(showBackground = true)
@Composable
fun GroupCreatePreview() {
    MaterialTheme {
        GroupCreate(rememberNavController())
    }
}