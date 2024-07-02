<template>

    <v-card outlined>
        <v-card-title>
            Dashboard # {{$route.params.id }}
        </v-card-title>

        <v-card-text>
            <div>
                <String label="ProductId" v-model="item.productId" :editMode="editMode" @change="change" />
            </div>
            <div>
                <Number label="QuestionCnt" v-model="item.questionCnt" :editMode="editMode" @change="change" />
            </div>
            <div>
                <Number label="RequestCnt" v-model="item.requestCnt" :editMode="editMode" @change="change" />
            </div>
            <div>
                <String label="MarketerId" v-model="item.marketerId" :editMode="editMode" @change="change" />
            </div>
        </v-card-text>
    </v-card>

</template>

<script>
  const axios = require('axios').default;

  export default {
    name: 'DashboardViewDetail',
    props: {
      value: Object,
    },
    data: () => ({
        item : [],
    }),
    async created() {
      var params = this.$route.params;
      var temp = await axios.get(axios.fixUrl('/dashboards/' + params.id))

      this.item = temp.data;

    },
    methods: {
    }
  }
</script>

