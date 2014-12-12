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

package de.openminds.training.giftsforfriends;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PseudoData {

    private static final List<ContactInformation> CONTACT_INFORMATIONS;
    private static final Map<String, List<Gift>> MAP_OF_GIFTS;

    static {
        CONTACT_INFORMATIONS = new ArrayList<>();
        CONTACT_INFORMATIONS.add(new ContactInformation(1L, "Beate", 5));
        CONTACT_INFORMATIONS.add(new ContactInformation(2L, "Frank", 2));
        MAP_OF_GIFTS = new HashMap<>();
        List<Gift> gifts = new ArrayList<>();
        gifts.add(new Gift("Concert tickets"));
        gifts.add(new Gift("Babysitter"));
        MAP_OF_GIFTS.put("Frank", gifts);
        gifts = new ArrayList<>();
        gifts.add(new Gift("Amusement park"));
        MAP_OF_GIFTS.put("Beate", gifts);
    }

    public static List<ContactInformation> getAllContactInformations() {
        return CONTACT_INFORMATIONS;
    }

    public static List<Gift> getAllGiftsForContact(long id) {
        if (id < 1) {
            return null;
        }
        return MAP_OF_GIFTS.get(CONTACT_INFORMATIONS.get((int)id - 1).name);
    }


    public static final class ContactInformation {
        public Long id;
        public String name;
        public int noOfGifts;
        public ContactInformation(Long id, String name, int gifts) {
            this.id = id;
            this.name = name;
            this.noOfGifts = gifts;
        }
    }

    public static final class Gift {
        public String name;
        public Gift(String name) {
            this.name = name;
        }
    }
}
