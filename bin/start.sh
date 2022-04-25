#!/bin/bash
bin_dir=$(dirname "$(readlink -f "$0")") && dos2unix ${bin_dir}/*.sh && echo "bin_dir : ${bin_dir}"
# 项目基本目录
base_dir=$(dirname "${bin_dir}")
echo "base_dir : ${base_dir}"
# 项目名，建议配置为 maven 的默认jar命名风格
app_full_name=$(basename "${base_dir}")
app_name=${app_full_name%-*}
echo "app_name : ${app_name}"
# jar包所在目录
lib_dir=${base_dir}/lib
echo "lib_dir : ${lib_dir}"
sh -c ${bin_dir}/stop.sh
# 配置jvm的堆内存
total_men=$(head /proc/meminfo -n 1 | awk '{print $2}')
echo "total_men : ${total_men} kb"
jvm_men=$((${total_men} / 1024 / 4))
if [ ${jvm_men} -lt 1024 ]; then jvm_men=1024; fi
echo "jvm_men : ${jvm_men} mb"
jvm_config="\
-Xms${jvm_men}m \
-Xmx${jvm_men}m \
-Xss256k \
-server \
-Djava.security.edg=file:/dev/./urandom \
-Dfile.encoding=UTF-8 \
-XX:+HeapDumpOnOutOfMemoryError \
-XX:HeapDumpPath=${base_dir}/ \
"
cd ${base_dir} || (echo "lib_dir not exist" && exit 1)
nohup java ${jvm_config} -jar ${lib_dir}/${app_full_name}.jar >/dev/null 2>&1 &
#java ${jvm_config} -jar ${lib_dir}/${app_full_name}.jar
echo "${app_name} has started ,but may be exist exception.please check log"
