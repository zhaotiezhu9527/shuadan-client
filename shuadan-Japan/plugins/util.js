export function dateFormat(fmt, date) {
  let ret;
  const opt = {
    "Y+": date.getFullYear().toString(), // 年
    "m+": (date.getMonth() + 1).toString(), // 月
    "d+": date.getDate().toString(), // 日
    "H+": date.getHours().toString(), // 时
    "M+": date.getMinutes().toString(), // 分
    "S+": date.getSeconds().toString(), // 秒
    // 有其他格式化字符需求可以继续添加，必须转化成字符串
  };
  for (let k in opt) {
    ret = new RegExp("(" + k + ")").exec(fmt);
    if (ret) {
      fmt = fmt.replace(
        ret[1],
        ret[1].length == 1 ? opt[k] : opt[k].padStart(ret[1].length, "0")
      );
    }
  }
  return fmt;
}

export function FormatAmount(num) {
  let result = [],
    counter = 0;
  num = (Math.floor(num) || 0).toString().split("");
  for (let i = num.length - 1; i >= 0; i--) {
    counter++;
    result.unshift(num[i]);
    if (!(counter % 3) && i != 0) {
      result.unshift(".");
    }
  }
  if (result[0] == "-" && result[1] == ".") {
    result.splice(1, 1);
  }
  return result.join("");
}
export function keepTwoDecimalFull(num) {
  var result = parseFloat(Number(num));
   if (isNaN(result)) { 
    // alert('传递参数错误，请检查！'); 
    return false; 
  } 
  result = Math.floor(num * 100) / 100; 
  var s_x = result.toString(); //将数字转换为字符串

  var pos_decimal = s_x.indexOf('.'); //小数点的索引值


  // 当整数时，pos_decimal=-1 自动补0  
  if (pos_decimal < 0) {
    pos_decimal = s_x.length; s_x += '.';
  }

  // 当数字的长度< 小数点索引+2时，补0  
  while (s_x.length <= pos_decimal + 2) {
    s_x += '0';
  }
  return s_x;
}

