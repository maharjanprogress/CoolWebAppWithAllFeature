export enum FileType {
  XLSX = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
  XLS = 'application/vnd.ms-excel',
  CSV = 'text/csv',
  PDF = 'application/pdf',
  DOC = 'application/msword',
  DOCX = 'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
  PNG = 'image/png',
  JPEG = 'image/jpeg',
  GIF = 'image/gif',
}

export const fileTypeIcons: { [key: string]: string } = {
  [FileType.XLSX]: 'icons/excel.svg',
  [FileType.XLS]: 'icons/excel.svg',
  [FileType.CSV]: 'icons/excel.svg',
  [FileType.PDF]: 'icons/pdf.svg',
  [FileType.DOC]: 'icons/word.svg',
  [FileType.DOCX]: 'icons/word.svg',
  [FileType.PNG]: 'icons/image.svg',
  [FileType.JPEG]: 'icons/image.svg',
  [FileType.GIF]: 'icons/image.svg',
  'default': 'icons/file.svg'
};

export const fileTypeExtensions: { [key: string]: string[] } = {
  [FileType.XLSX]: ['.xlsx'],
  [FileType.XLS]: ['.xls'],
  [FileType.CSV]: ['.csv'],
  [FileType.PDF]: ['.pdf'],
  [FileType.DOC]: ['.doc'],
  [FileType.DOCX]: ['.docx'],
  [FileType.PNG]: ['.png'],
  [FileType.JPEG]: ['.jpg', '.jpeg'],
  [FileType.GIF]: ['.gif'],
};
