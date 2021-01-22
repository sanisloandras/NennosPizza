package com.sanislo.nennospizza

import com.google.gson.Gson
import com.sanislo.nennospizza.api.data.DataService
import com.sanislo.nennospizza.api.data.ResourceResponse
import com.sanislo.nennospizza.api.data.PizzasResponse

class FakeDataService  : DataService {
    override suspend fun ingredients(): ResourceResponse {
        return Gson().fromJson(INGREDIENTS, ResourceResponse::class.java)
    }

    override suspend fun drinks(): ResourceResponse {
        return Gson().fromJson(DRINKS, ResourceResponse::class.java)
    }

    override suspend fun pizzas(): PizzasResponse {
        return Gson().fromJson(PIZZAS, PizzasResponse::class.java)
    }

    companion object {
        const val DRINKS = "[\n" +
                "  {\n" +
                "    \"price\": 1,\n" +
                "    \"name\": \"Still Water\",\n" +
                "    \"id\": 1\n" +
                "  },\n" +
                "  {\n" +
                "    \"price\": 1.5,\n" +
                "    \"name\": \"Sparkling Water\",\n" +
                "    \"id\": 2\n" +
                "  },\n" +
                "  {\n" +
                "    \"price\": 2.5,\n" +
                "    \"name\": \"Coke\",\n" +
                "    \"id\": 3\n" +
                "  },\n" +
                "  {\n" +
                "    \"price\": 3,\n" +
                "    \"name\": \"Beer\",\n" +
                "    \"id\": 4\n" +
                "  },\n" +
                "  {\n" +
                "    \"price\": 4,\n" +
                "    \"name\": \"Red Wine\",\n" +
                "    \"id\": 5\n" +
                "  }\n" +
                "]"

        const val INGREDIENTS = "[\n" +
                "  {\n" +
                "    \"price\": 1,\n" +
                "    \"name\": \"Mozzarella\",\n" +
                "    \"id\": 1\n" +
                "  },\n" +
                "  {\n" +
                "    \"price\": 0.5,\n" +
                "    \"name\": \"Tomato Sauce\",\n" +
                "    \"id\": 2\n" +
                "  },\n" +
                "  {\n" +
                "    \"price\": 1.5,\n" +
                "    \"name\": \"Salami\",\n" +
                "    \"id\": 3\n" +
                "  },\n" +
                "  {\n" +
                "    \"price\": 2,\n" +
                "    \"name\": \"Mushrooms\",\n" +
                "    \"id\": 4\n" +
                "  },\n" +
                "  {\n" +
                "    \"price\": 4,\n" +
                "    \"name\": \"Ricci\",\n" +
                "    \"id\": 5\n" +
                "  },\n" +
                "  {\n" +
                "    \"price\": 2,\n" +
                "    \"name\": \"Asparagus\",\n" +
                "    \"id\": 6\n" +
                "  },\n" +
                "  {\n" +
                "    \"price\": 1,\n" +
                "    \"name\": \"Pineapple\",\n" +
                "    \"id\": 7\n" +
                "  },\n" +
                "  {\n" +
                "    \"price\": 3,\n" +
                "    \"name\": \"Speck\",\n" +
                "    \"id\": 8\n" +
                "  },\n" +
                "  {\n" +
                "    \"price\": 2.5,\n" +
                "    \"name\": \"Bottarga\",\n" +
                "    \"id\": 9\n" +
                "  },\n" +
                "  {\n" +
                "    \"price\": 2.2,\n" +
                "    \"name\": \"Tuna\",\n" +
                "    \"id\": 10\n" +
                "  }\n" +
                "]"

        const val PIZZAS = "{\n" +
                "  \"basePrice\": 4,\n" +
                "  \"pizzas\": [\n" +
                "    {\n" +
                "      \"ingredients\": [\n" +
                "        1,\n" +
                "        2\n" +
                "      ],\n" +
                "      \"name\": \"Margherita\",\n" +
                "      \"imageUrl\": \"https://cdn.pbrd.co/images/M57yElqQo.png\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"ingredients\": [\n" +
                "        1,\n" +
                "        5\n" +
                "      ],\n" +
                "      \"name\": \"Ricci\",\n" +
                "      \"imageUrl\": \"https://cdn.pbrd.co/images/M58jWCFVC.png\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"ingredients\": [\n" +
                "        1,\n" +
                "        2,\n" +
                "        3,\n" +
                "        4\n" +
                "      ],\n" +
                "      \"name\": \"Boscaiola\",\n" +
                "      \"imageUrl\": \"https://cdn.pbrd.co/images/tOhJQ5N3.png\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"ingredients\": [\n" +
                "        1,\n" +
                "        5,\n" +
                "        6\n" +
                "      ],\n" +
                "      \"name\": \"Primavera\",\n" +
                "      \"imageUrl\": \"https://cdn.pbrd.co/images/M57VcfLGQ.png\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"ingredients\": [\n" +
                "        1,\n" +
                "        2,\n" +
                "        7,\n" +
                "        8\n" +
                "      ],\n" +
                "      \"name\": \"Hawaii\",\n" +
                "      \"imageUrl\": \"https://cdn.pbrd.co/images/M57lNSLnC.png\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"ingredients\": [\n" +
                "        1,\n" +
                "        9,\n" +
                "        10\n" +
                "      ],\n" +
                "      \"name\": \"Mare Bianco\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"ingredients\": [\n" +
                "        1,\n" +
                "        2,\n" +
                "        4,\n" +
                "        8,\n" +
                "        9,\n" +
                "        10\n" +
                "      ],\n" +
                "      \"name\": \"Mari e monti\",\n" +
                "      \"imageUrl\": \"https://cdn.pbrd.co/images/M57K6OFiU.png\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"ingredients\": [\n" +
                "        1,\n" +
                "        9\n" +
                "      ],\n" +
                "      \"name\": \"Bottarga\",\n" +
                "      \"imageUrl\": \"https://cdn.pbrd.co/images/M57aGTmgA.png\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"ingredients\": [\n" +
                "        1,\n" +
                "        2,\n" +
                "        9,\n" +
                "        6\n" +
                "      ],\n" +
                "      \"name\": \"Boottarga e Asparagi\",\n" +
                "      \"imageUrl\": \"https://cdn.pbrd.co/images/4O6T9RQLX.png\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"ingredients\": [\n" +
                "        1,\n" +
                "        5,\n" +
                "        6\n" +
                "      ],\n" +
                "      \"name\": \"Ricci e Asparagi\",\n" +
                "      \"imageUrl\": \"https://cdn.pbrd.co/images/4O70NDkMl.png\"\n" +
                "    }\n" +
                "  ]\n" +
                "}"
    }
}