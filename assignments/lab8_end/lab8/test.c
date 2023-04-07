#include<stdio.h>
#include <omp.h>

int main (int c, char** v){

#pragma omp parallel
    printf("test\n");
    return 0;
}
