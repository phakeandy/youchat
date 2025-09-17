#!/bin/bash

if [ $# -eq 0 ]; then
    echo "Usage: $0 <description>"
    exit 1
fi

VERSION=$(date +%y%m%d.%H%M)
DESCRIPTION=$(echo "$*" | tr ' ' '_')
MIGRATION_DIR="src/main/resources/db/migration"

# 检查是否已存在相同版本的迁移文件
count=0
existing_files=(${MIGRATION_DIR}/V${VERSION}*.sql)
if [ -f "${existing_files[0]}" ]; then
    # 找到最大的序号
    for file in "${existing_files[@]}"; do
        basename=$(basename "$file")
        if [[ $basename =~ V${VERSION}_([0-9]+)__.+\.sql ]]; then
            seq_num=${BASH_REMATCH[1]}
            if [ $seq_num -gt $count ]; then
                count=$seq_num
            fi
        elif [[ $basename =~ V${VERSION}__.+\.sql ]]; then
            count=0
        fi
    done
    count=$((count + 1))
    FILENAME="V${VERSION}_${count}__${DESCRIPTION}.sql"
else
    FILENAME="V${VERSION}__${DESCRIPTION}.sql"
fi

MIGRATION_PATH="${MIGRATION_DIR}/${FILENAME}"

touch "${MIGRATION_PATH}"
echo "Created migration: ${FILENAME}"