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
jvm_config="\
-Xms128m \
-Xmx128m \
-Xss256k \
-server \
-Djava.security.edg=file:/dev/./urandom \
-Dfile.encoding=UTF-8 \
-XX:+HeapDumpOnOutOfMemoryError \
-XX:HeapDumpPath=${base_dir}/ \
"
cd ${base_dir} || (echo "lib_dir not exist" && exit 1)
java ${jvm_config} -jar ${lib_dir}/${app_full_name}.jar
