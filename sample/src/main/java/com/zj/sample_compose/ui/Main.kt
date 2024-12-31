package com.zj.sample_compose.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

val titles = listOf(
    "扫描蓝牙设备",
    "获取焊机信息",
    "授权人员信息",
    "拍照完成信息",
    "授权工程信息",
    "焊口编号验证",
    "获取焊口焊接信息",
)

@Composable
fun Main(
    viewModel: MainViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        editItem(
            title = "焊机编号：",
            value = uiState.welderNum,
            onValueChange = {
                viewModel.onWelderNumChange(it)
            }
        )
        editItem(
            title = "焊机操作员编号：",
            value = uiState.operatorNum,
            onValueChange = {
                viewModel.onOperatorNumChange(it)
            }
        )
        editItem(
            title = "拍照请求序号：",
            value = uiState.requestCode,
            onValueChange = {
                viewModel.onRequestCodeChange(it)
            }
        )
        editItem(
            title = "拍照结果：",
            value = uiState.resultCode,
            onValueChange = {
                viewModel.onResultCodeChange(it)
            },
            supportingText = "1：焊接合格 2：焊接不合格"
        )
        editItem(
            title = "工程编号：",
            value = uiState.projectNum,
            onValueChange = {
                viewModel.onProjectNumChange(it)
            }
        )
        editItem(
            title = "焊口编号：",
            value = uiState.weldNum,
            onValueChange = {
                viewModel.onWeldNumChange(it)
            }
        )
        editItem(
            title = "焊口编号验证结果：",
            value = uiState.weldNumCheckResult,
            onValueChange = {
                viewModel.onWeldNumCheckResultChange(it)
            },
            supportingText = "0：成功 1：失败"
        )
        editItem(
            title = "业务焊口号（唯一ID）：",
            value = uiState.weldingPortNum,
            onValueChange = {
                viewModel.onWeldingPortNumChange(it)
            }
        )
        items(
            items = titles,
            key = { it }
        ) { item ->
            Button(
                onClick = {
                    viewModel.onClick(item)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(item)
            }
        }
        items(
            items = uiState.deviceList,
            key = { it.bleDevice.mac },
            span = { GridItemSpan(2) }
        ) { item ->
            ListItem(
                headlineContent = {
                    Text(item.bleDevice.name)
                },
                trailingContent = {
                    when (item.connectStatus) {
                        1 -> {
                            Text("连接中...", color = Color(0xFF2A2A2A))
                        }

                        2 -> {
                            Text("已连接", color = Color(0xFF1CC221))
                        }

                        3 -> {
                            Text("连接断开", color = Color(0xFF2B2B2B))
                        }

                        -1 -> {
                            Text("连接失败", color = Color(0xFFC90000))
                        }
                    }
                },
                modifier = Modifier.clickable {
                    viewModel.connect(item)
                }
            )
        }
        items(
            items = uiState.messageList,
            span = { GridItemSpan(2) },
        ) { item ->
            Text(item, style = MaterialTheme.typography.bodySmall)
        }
    }
}

private fun LazyGridScope.editItem(
    title: String,
    value: String,
    supportingText: String? = null,
    onValueChange: (String) -> Unit
) {
    item(
        span = { GridItemSpan(2) },
        contentType = title
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                supportingText = {
                    if (supportingText != null) {
                        Text(supportingText)
                    }
                }
            )
        }
    }
}