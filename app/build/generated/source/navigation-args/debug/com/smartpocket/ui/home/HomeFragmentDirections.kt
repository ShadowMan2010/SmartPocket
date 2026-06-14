package com.smartpocket.ui.home

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.smartpocket.R

public class HomeFragmentDirections private constructor() {
  public companion object {
    public fun actionHomeToFace(): NavDirections = ActionOnlyNavDirections(R.id.action_home_to_face)

    public fun actionHomeToBluetooth(): NavDirections =
        ActionOnlyNavDirections(R.id.action_home_to_bluetooth)
  }
}
