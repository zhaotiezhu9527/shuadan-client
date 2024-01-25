const TransformPages = require("uni-read-pages");

const { webpack } = new TransformPages();
module.exports = {
  configureWebpack: {
    plugins: [
      new webpack.DefinePlugin({
        ROUTES: webpack.DefinePlugin.runtimeValue(() => {
          const tfPages = new TransformPages({
            includes: ["path", "name", "aliasPath"],
          });
          return JSON.stringify(tfPages.routes);
        }, true),
      }),
    ],
  },
};
if (process.env.UNI_PLATFORM === 'h5') {
  let filePath = 'static/js/'
  let Timestamp = new Date().getTime()
  process.env.VUE_APP_INDEX_CSS_HASH = `${Timestamp}`
  // module.exports = {
  //     // webpack配置
  //     filenameHashing: false,
  //     configureWebpack: { // webpack 配置 解决js缓存的问题
  //         output: { // 输出重构  打包编译后的 文件目录/文件名称?v=时间戳
  //             filename: `${filePath}[name].js?v=${Timestamp}`,
  //             chunkFilename: `${filePath}[name].js?v=${Timestamp}`
  //         },
  //     }
  // }
} else {
  // 其他打包配置
  // module.exports = {
  //     // webpack 相关配置
  //     filenameHashing: false
  // }
}