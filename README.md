# vkgl-vcf-converter
Converts http://molgenis.org/vkgl downloads to VCF (Variant Call Format) files

## Usage
Navigate to http://molgenis.org/vkgl and download an export file via the Downloads menu.

java -jar vkgl-vcf-converter.jar
usage: java -jar vkgl-vcf-converter.jar
 -f,--force          Override the output file if it already exists.
 -h,--help           Show help message.
 -i,--input <arg>    Input VKGL export (.csv).
 -o,--output <arg>   Output VKGL export (.vcf).
 -v,--verbose        Print debug information.
