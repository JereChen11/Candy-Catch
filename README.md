# 前言
回想小时候，一到冬天就开始期盼着学校快点放寒假，期盼着快点过年。因为过年有放不完的鞭炮与吃不完的糖果，犹记得那时候我的口袋里总是充满着各式各样的糖果。今天就以糖果为主题，实现 **糖果捕捉器** 这个互动小游戏，希望你的生活如糖果般甜蜜。


# 效果展示
游戏包含三个版本，难度逐步上升：
* 基础版：糖果只会在屏幕最上方生成，然后从上往下掉落。
* 进阶版：糖果只会在屏幕中间生成，然后向四周发散
* 困难版：糖果会在屏幕四个角随机生成，然后向大致对角方向发散。

| 基础版 | 进阶版 | 困难版 |
| :---: | :---: | :---: |
| <img src="https://raw.githubusercontent.com/JereChen11/Candy-Catch/main/image/basic.gif"> | <img src="https://raw.githubusercontent.com/JereChen11/Candy-Catch/main/image/advanced.gif"> | <img src="https://raw.githubusercontent.com/JereChen11/Candy-Catch/main/image/difficult.gif"> |


# 实现细节
该游戏依赖 `Kotlin` + 动画得以实现，实现难点主要在计算糖果的平移距离。我们需要随机生成糖果，并利用相似三角形计算属性动画的平移距离，并且需要考虑到距离不足以支持糖果平移出手机屏幕的情况。

| 随机坐标 | 相似三角形 | 距离不足 |
| :---: | :---: | :---: |
| <img src="https://raw.githubusercontent.com/JereChen11/Candy-Catch/main/image/coordinate.png"> | <img src="https://raw.githubusercontent.com/JereChen11/Candy-Catch/main/image/advanced_left_top.png"> | <img src="https://raw.githubusercontent.com/JereChen11/Candy-Catch/main/image/not_enough.png"> |

<br>更多具体的实现细节请参考我的这两篇博客：
* [kotlin + 动画实现一款小游戏，快速上手](https://juejin.cn/post/7054194708410531876)
* [糖果捕捉器进阶版与困难版来啦！](https://juejin.cn/post/7088536853959147533)


# 总结
如果你对该小游戏感兴趣，欢迎点击我的文章或查看源码来了解实现原理。当然，如果你有任何疑惑，欢迎留言与我一起探讨。<br>
<br>最后，如果可以，欢迎你给我点个小星星，非常感谢！
