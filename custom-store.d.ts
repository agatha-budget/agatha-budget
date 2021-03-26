import { ComponentCustomProperties } from 'vue'
import { Store, StoreState } from 'vuex'

declare module '@vue/runtime-core' {
  // provide typings for `this.$store`
  interface ComponentCustomProperties {
    $store: Store<StoreState>
  }
}
