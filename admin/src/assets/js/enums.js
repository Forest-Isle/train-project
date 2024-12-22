const PASSENGER_TYPE={ADULT:{code:"1",desc:"成人"},CHILD:{code:"2",desc:"儿童"},STUDENT:{code:"3",desc:"学生"}};
const TRAIN_TYPE={G:{code:"G",desc:"高铁",priceRate:"1.2"},D:{code:"D",desc:"动车",priceRate:"1"},K:{code:"K",desc:"快速",priceRate:"0.8"}};
const SEAT_TYPE={YDZ:{code:"1",desc:"一等座",price:"0.4"},EDZ:{code:"2",desc:"二等座",price:"0.3"},RW:{code:"3",desc:"软卧",price:"0.6"},YW:{code:"4",desc:"硬卧",price:"0.5"}};
const SEAT_COL={YDZ_A:
    {code:"A",desc:"A",type:"1"},
    YDZ_C:{code:"C",desc:"C",type:"1"},
    YDZ_D:{code:"D",desc:"D",type:"1"},
    YDZ_F:{code:"F",desc:"F",type:"1"},
    EDZ_A:{code:"A",desc:"A",type:"2"},
    EDZ_B:{code:"B",desc:"B",type:"2"},
    EDZ_C:{code:"C",desc:"C",type:"2"},
    EDZ_D:{code:"D",desc:"D",type:"2"},
    EDZ_F:{code:"F",desc:"F",type:"2"},
    RW_A:{code:"A",desc:"A", type:"3"},
    RW_B:{code:"B",desc: "B",type:"3"},
    RW_C:{code:"C",desc: "C",type:"3"},
    RW_D:{code:"D",desc: "D",type:"3"},
    RW_E:{code:"E",desc: "E",type: "3"},
    RW_F:{code:"F",desc:"F", type:"3"},
    YW_A:{code:"A",desc: "A",type: "4"},
    YW_B:{code:"B",desc: "B",type: "4"},
    YW_C:{code:"C",desc: "C",type: "4"},
    YW_D:{code:"D",desc: "D",type: "4"},
    YW_E:{code:"E",desc: "E",type: "4"},
    YW_F:{code:"F",desc: "F",type: "4"},
    YW_G:{code:"G",desc: "G",type: "4"},
    YW_H:{code:"H",desc:"H", type:"4"}};
const CONFIRM_ORDER_STATUS={INIT:{code:"I",desc:"初始"},PENDING:{code:"P",desc:"处理中"},SUCCESS:{code:"S",desc:"成功"},FAILURE:{code:"F",desc:"失败"},EMPTY:{code:"E",desc:"无票"},CANCEL:{code:"C",desc:"取消"}};

const PASSENGER_TYPE_ARRAY=[{code:"1",desc:"成人"},{code:"2",desc:"儿童"},{code:"3",desc:"学生"}];
const TRAIN_TYPE_ARRAY=[{code:"G",desc:"高铁",priceRate:"1.2"},{code:"D",desc:"动车",priceRate:"1"},{code:"K",desc:"快速",priceRate:"0.8"}];
const SEAT_TYPE_ARRAY=[{code:"1",desc:"一等座",price:"0.4"},{code:"2",desc:"二等座",price:"0.3"},{code:"3",desc:"软卧",price:"0.6"},{code:"4",desc:"硬卧",price:"0.5"}];
const SEAT_COL_ARRAY=[{code:"A",desc:"A",type:"1"},{code:"C",desc:"C",type:"1"},{code:"D",desc:"D",type:"1"},{code:"F",desc:"F",type:"1"},{code:"A",desc:"A",type:"2"},{code:"B",desc:"B",type:"2"},{code:"C",desc:"C",type:"2"},{code:"D",desc:"D",type:"2"},{code:"F",desc:"F",type:"2"},{code:"A",desc:"A", type:"3"},
    {code:"B",desc: "B",type:"3"},
    {code:"C",desc: "C",type:"3"},
    {code:"D",desc: "D",type:"3"},
    {code:"E",desc: "E",type: "3"},
    {code:"F",desc:"F", type:"3"},
    {code:"A",desc: "A",type: "4"},
    {code:"B",desc: "B",type: "4"},
    {code:"C",desc: "C",type: "4"},
    {code:"D",desc: "D",type: "4"},
    {code:"E",desc: "E",type: "4"},
    {code:"F",desc: "F",type: "4"},
    {code:"G",desc: "G",type: "4"},
    {code:"H",desc:"H", type:"4"}];
const CONFIRM_ORDER_STATUS_ARRAY=[{code:"I",desc:"初始"},{code:"P",desc:"处理中"},{code:"S",desc:"成功"},{code:"F",desc:"失败"},{code:"E",desc:"无票"},{code:"C",desc:"取消"}];

window.PASSENGER_TYPE = PASSENGER_TYPE;
window.TRAIN_TYPE = TRAIN_TYPE;
window.SEAT_TYPE = SEAT_TYPE;
window.SEAT_COL = SEAT_COL;
window.CONFIRM_ORDER_STATUS = CONFIRM_ORDER_STATUS;

window.PASSENGER_TYPE_ARRAY = PASSENGER_TYPE_ARRAY;
window.TRAIN_TYPE_ARRAY = TRAIN_TYPE_ARRAY;
window.SEAT_TYPE_ARRAY = SEAT_TYPE_ARRAY;
window.SEAT_COL_ARRAY = SEAT_COL_ARRAY;
window.CONFIRM_ORDER_STATUS_ARRAY = CONFIRM_ORDER_STATUS_ARRAY;

export {
    PASSENGER_TYPE,
    TRAIN_TYPE,
    SEAT_TYPE,
    SEAT_COL,
    CONFIRM_ORDER_STATUS,
    PASSENGER_TYPE_ARRAY,
    TRAIN_TYPE_ARRAY,
    SEAT_TYPE_ARRAY,
    SEAT_COL_ARRAY,
    CONFIRM_ORDER_STATUS_ARRAY,
}