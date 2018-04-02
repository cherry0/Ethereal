package cherry.ethereal.data.MusicUnit;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

/**
 * Copyright (C) 2015 Ari C.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class MusicSuggestion implements SearchSuggestion {

    private String mMusicName;
    private boolean mIsHistory = false;

    public MusicSuggestion(String suggestion) {
        this.mMusicName = suggestion.toLowerCase();
    }

    public MusicSuggestion(Parcel source) {
        this.mMusicName = source.readString();
        this.mIsHistory = source.readInt() != 0;
    }

    public void setIsHistory(boolean isHistory) {
        this.mIsHistory = isHistory;
    }

    public boolean getIsHistory() {
        return this.mIsHistory;
    }

    @Override
    public String getBody() {
        return mMusicName;
    }

    public static final Creator<MusicSuggestion> CREATOR = new Creator<MusicSuggestion>() {
        @Override
        public MusicSuggestion createFromParcel(Parcel in) {
            return new MusicSuggestion(in);
        }

        @Override
        public MusicSuggestion[] newArray(int size) {
            return new MusicSuggestion[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mMusicName);
        dest.writeInt(mIsHistory ? 1 : 0);
    }
}