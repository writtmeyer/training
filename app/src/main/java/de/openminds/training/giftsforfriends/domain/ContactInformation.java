/*
 * Copyright 2014 Wolfram Rittmeyer / OpenMinds UG (haftungsbeschränkt)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.openminds.training.giftsforfriends.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class stores a ContactInformation. It also implements Parcelable
 * with all the boilerplate that is needed for Parcelables to
 * work reliably.
 */
public class ContactInformation implements Parcelable {
    public Long id;
    public String name;
    public int noOfGifts;

    public ContactInformation(Long id, String name, int gifts) {
        this.id = id;
        this.name = name;
        this.noOfGifts = gifts;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        // could be null, but you cannot safely store null,
        // using -1 is a way to circumvent this
        if (id == null) {
            parcel.writeLong(-1L);
        }
        else {
            parcel.writeLong(id);
        }
        // again we need a way to detect a null value
        if (name == null) {
            parcel.writeInt(-1);
        }
        else {
            parcel.writeInt(0);
            parcel.writeString(name);
        }
        // the primitive value noOfGifts cannot be null
        parcel.writeInt(noOfGifts);
    }

    public ContactInformation(Parcel parcel) {
        // first check for null value
        long _id = parcel.readLong();
        if (_id == -1) {
            this.id = null;
        }
        else {
            this.id = _id;
        }
        // read null flag
        int nameflag = parcel.readInt();
        if (nameflag == -1) {
            this.name = null;
        }
        else {
            this.name = parcel.readString();
        }
        // no need for null tests for the primitive noOfGifts
        this.noOfGifts = parcel.readInt();
    }

    public static final Parcelable.Creator<ContactInformation> CREATOR = new Parcelable.Creator<ContactInformation>() {
        @Override
        public ContactInformation createFromParcel(Parcel parcel) {
            return new ContactInformation(parcel);
        }

        @Override
        public ContactInformation[] newArray(int i) {
            return new ContactInformation[i];
        }
    };

}
