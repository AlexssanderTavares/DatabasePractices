package org.example.cahousing.ViewModels.UIStates

import org.example.cahousing.DataSource.Models.Dept

data class DepartmentsViewUIState(
    var visibility: Boolean = false,
    var deptList: ArrayList<Dept> = arrayListOf(),
    var onVisibilityChange: (Boolean) -> Unit = {},
    var onListUpdate: (ArrayList<Dept>) -> Unit = {}
)
