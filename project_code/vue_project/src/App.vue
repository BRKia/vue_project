<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<style>
</style>
<script>
  export default {
    data(){
      return {
        currentPathName: localStorage.getItem("currentPathName")
      }
    },
  }

</script>