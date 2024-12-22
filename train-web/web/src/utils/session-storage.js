// 所有的 session key 统一定义
export const SESSION_ORDER = "SESSION_ORDER";
export const SESSION_TICKET_PARAMS = "SESSION_TICKET_PARAMS";

const SessionStorage = {
  get: function (key, defaultValue = null) {
    if (typeof window === 'undefined' || !window.sessionStorage) {
      return defaultValue; // 非浏览器环境返回默认值
    }
    try {
      const v = sessionStorage.getItem(key);
      if (v && v !== "undefined") {
        return JSON.parse(v);
      }
    } catch (e) {
      console.warn(`SessionStorage.get: Failed to parse value for key "${key}"`, e);
    }
    return defaultValue; // 解析失败或值不存在时返回默认值
  },

  set: function (key, data) {
    if (typeof window !== 'undefined' && window.sessionStorage) {
      sessionStorage.setItem(key, JSON.stringify(data));
    }
  },

  remove: function (key) {
    if (typeof window !== 'undefined' && window.sessionStorage) {
      sessionStorage.removeItem(key);
    }
  },

  clearAll: function () {
    if (typeof window !== 'undefined' && window.sessionStorage) {
      sessionStorage.clear();
    }
  }
};

export default SessionStorage;
