export interface SimilarityReport {
  passed: boolean;
  registered: boolean;
  tampered: boolean;
  checkResultsList: CheckResults[];
}

export interface CheckResults {
  keyPointSimilarity: number;
  histogramSimilarity: number;
  metadataSimilarity: number;
  owner: string;
  ownerId: string
  imageName: string;
  imageId: string;
  metadata: Metadata;
  table?: any;
  tampered: boolean;
}

export interface MetadataElement {
  key: string;
  value: string;
}

export interface Metadata {
  metadata: MetadataElement[];
}

export interface MessageDialogData {
  image: string;
  userId: string;
  from: string;
  text: string;
  imageName: string;
  userName: string;
}

export interface MessageModel {
  from: string;
  image: string;
  text: string;
  date: string;
}
