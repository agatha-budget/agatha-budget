<template>
<div class="main container-fluid">
  <div class="main-row">
  <router-view/>
  </div>
</div>
</template>
<style lang="less">
.main {
  height: 100vh;
};
.main-row {
  height: 100%;
};
@import "~bootstrap/dist/css/bootstrap.min.css";
@import "style/atomic_design/atomic_design.less";
</style>
