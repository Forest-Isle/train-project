import { createStore } from 'vuex'
import SessionStorage from '../utils/session-storage'; // 根据实际文件路径调整
 
const MEMBER = "MEMBER";

export default createStore({
  state: {
    member: SessionStorage.get(MEMBER) || {}
  },
  getters: {
  },
  mutations: {
    setMember (state, _member) {
      state.member = _member;
      SessionStorage.set(MEMBER, _member);
    }
  },
  actions: {
  },
  modules: {
  }
})
