/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tournament.helper.tournaments;

import android.databinding.BindingAdapter;
import android.widget.ListView;

import com.tournament.helper.data.Tournament;

import java.util.List;

/**
 * Contains {@link BindingAdapter}s for the {@link Tournament} list.
 */
public class TournamentsListBindings {
    //TODO move to a databinding package

    @SuppressWarnings("unchecked")
    @BindingAdapter("app:items")
    public static void setItems(ListView listView, List<Tournament> items) {
        TournamentsFragment.TournamentsAdapter adapter = (TournamentsFragment.TournamentsAdapter) listView.getAdapter();
        if (adapter != null)
        {
            adapter.replaceData(items);
        }
    }
}
