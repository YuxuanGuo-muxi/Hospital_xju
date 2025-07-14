import { createStore } from 'vuex'

const initialAccount = localStorage.getItem('account') || '';

export default createStore({
  state: {
    account: initialAccount
  },
  mutations: {
    setAccount(state, account) {
        state.account = account;
        // 在 mutation 中更新本地存储
        localStorage.setItem('account', account);
    },
    setIdentity1(state, identity1) {
        state.identity1 = identity1;
        // 在 mutation 中更新本地存储
        localStorage.setItem('identity1', identity1);
    }
  },
  actions: {
  },
  modules: {
  }
})

