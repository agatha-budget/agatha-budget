<template>
  <div class="container header import row">
    <div class="containerCross col-12">
      <span class="cross fas fa-times-circle" v-on:click="closeImport"/>
    </div>
    <div class="importPage">
      <div v-if="fileImported">
        <p class="text">{{ this.nbOperationImported }}{{ $t('IMPORTED_OPERATION') }}</p>
      </div>
      <div v-else>
        <div v-if="fileSelected">
          <p class="text">{{ $t('FILE_IS_SELECTED') }}</p>
          <btn v-on:click="importOfxFile()" class="actionButton">{{ $t('IMPORT') }}</btn>
        </div>
        <div v-else>
          <div class="actionButton ofxForm">
            <input id="importOfxFile" type="file" @change="onFileChange($event)" class="inputOfx" />
            <label for="importOfxFile" class="col-12">{{ $t('SELECT_FILE') }}</label>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import OperationService from '@/services/OperationService'

interface ImportOfxData {
    fileOfx: any;
    fileImported: boolean;
    nbOperationImported: string;
}

export default defineComponent({
  name: 'ImportOfx',
  components: {},
  props: {
    accountId: {
      type: String,
      required: true
    }
  },
  computed: {
    fileSelected (): boolean {
      if (this.fileOfx === '') {
        return false
      } else {
        return true
      }
    }
  },
  data (): ImportOfxData {
    return {
      fileOfx: '',
      fileImported: false,
      nbOperationImported: '0'
    }
  },
  emits: ['closeImport'],
  methods: {
    onFileChange (event: { target: { files: any[] } }) {
      const file = event.target.files[0]
      this.fileOfx = file
    },
    async importOfxFile () {
      const fr = new FileReader()
      fr.readAsText(this.fileOfx, 'UTF-8')
      fr.onload = async (evt) => {
        if (evt.target) {
          console.log(evt.target.result)
          if (evt.target.result) {
            const ofx: string = evt.target.result.toString()
            this.nbOperationImported = await OperationService.importOfxFile(this.$store, this.accountId, ofx)
            this.fileImported = true
          }
        }
      }
      fr.onerror = (evt) => {
        console.error('Failed to read this file')
      }
    },
    closeImport () {
      this.$emit('closeImport')
    }
  }
})
</script>
