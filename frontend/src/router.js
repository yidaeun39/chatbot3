
import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router);


import ChatChatManager from "./components/listers/ChatChatCards"
import ChatChatDetail from "./components/listers/ChatChatDetail"

import TrainTrainManager from "./components/listers/TrainTrainCards"
import TrainTrainDetail from "./components/listers/TrainTrainDetail"


import MarketingMarketingManager from "./components/listers/MarketingMarketingCards"
import MarketingMarketingDetail from "./components/listers/MarketingMarketingDetail"


export default new Router({
    // mode: 'history',
    base: process.env.BASE_URL,
    routes: [
            {
                path: '/chats/chats',
                name: 'ChatChatManager',
                component: ChatChatManager
            },
            {
                path: '/chats/chats/:id',
                name: 'ChatChatDetail',
                component: ChatChatDetail
            },

            {
                path: '/trains/trains',
                name: 'TrainTrainManager',
                component: TrainTrainManager
            },
            {
                path: '/trains/trains/:id',
                name: 'TrainTrainDetail',
                component: TrainTrainDetail
            },


            {
                path: '/marketings/marketings',
                name: 'MarketingMarketingManager',
                component: MarketingMarketingManager
            },
            {
                path: '/marketings/marketings/:id',
                name: 'MarketingMarketingDetail',
                component: MarketingMarketingDetail
            },



    ]
})
