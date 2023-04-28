<template>
    <div></div>
</template>

<script>
    const axios = require('axios').default;

    export default{
        name: 'list-base',
        props: {
            offline: Boolean
        },
        computed: {
            filteredLists() {
                if (this.isUpdating) {
                    return [];
                }
                return this.values;
            },
        },
        data: () => ({
            values: [],
            newValue: {},
            isUpdating: false,

        }),
        methods:{
            closeDialog(){
                this.openDialog = false
            },
            append(value){
                this.tick = false
                this.newValue = {}
                this.values.push(value)
                
                this.$emit('input', this.values);

                this.$nextTick(function(){
                    this.tick=true
                })
            },
            remove(value){
                var where = -1;
                for(var i=0; i<this.values.length; i++){
                    if(this.values[i]._links.self.href == value._links.self.href){
                        where = i;
                        break;
                    }
                }

                if(where > -1){
                    this.values.splice(i, 1);
                    this.$emit('input', this.values);
                }
            },
            async updateNames() {
                for (let value of this.values) {
                    if (value.employeeId && value.employeeId.id) {
                        value.employeeId = await this.employeeToName(value.employeeId.id);
                    }
                    if (value.departmentId && value.departmentId.id) {
                        value.departmentId = await this.departmentToName(value.departmentId.id);
                    }
                    if (value.rankId && value.rankId.id) {
                        value.rankId = await this.rankToName(value.rankId.id);
                    }
                }
            },
            async employeeToName(id){
               try{
                    let result = await axios.get(axios.fixUrl(`/employees/${id}`))
                    result.data.id = id;
                    return result.data;
                }catch(e){
                    return null;
                }
            },
            async departmentToName(id){
               try{
                    let result = await axios.get(axios.fixUrl(`/departments/${id}`))
                    result.data.id = id;
                    return result.data;
                }catch(e){
                    return null;
                }
            },
            async rankToName(id){
               try{
                    let result = await axios.get(axios.fixUrl(`/ranks/${id}`))
                    result.data.id = id;
                    return result.data;
                }catch(e){
                    return null;
                }
            },
        },

    }

</script>